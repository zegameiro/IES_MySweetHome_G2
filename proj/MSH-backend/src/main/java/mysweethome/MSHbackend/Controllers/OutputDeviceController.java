package mysweethome.MSHbackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.LinkedList;
import org.json.JSONObject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/outputs")
@Tag(name = "Output device Endpoints")
public class OutputDeviceController {

    @Autowired
    private OutputDeviceService outputDevService;

    @Autowired
    private RoomService roomService;

    @Operation(summary = "Add a new output device", description = "Add a new output device with a choosen category and a initial with a initial state")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a Output Device instance")
    })
    @PostMapping("/add")
    public ResponseEntity<OutputDevice> addOutputDevice(@RequestParam String name, @RequestParam String state, @RequestParam int category) {
        OutputDevice dev = new OutputDevice();

        OutputDeviceType dev_category = OutputDeviceType.valueOf(category);
        dev.setName(name);
        dev.setCurrent_state(state);
        dev.setDevice_category(dev_category);
        dev.setLaststatechange(System.currentTimeMillis());
        dev.setDevice_location("None");
        dev.setSlider_value("0");

        switch (dev_category) {
            case AIR_CONDITIONER:
                dev.setTemperature(0); // default temperature 0
                break;
            case TELEVISION:
                dev.setCurrent_channel("None");
                break;
            case SPEAKER:
                dev.setCurrent_music("None");
                break;
            case LIGHT:
                dev.setColor("white");
                break;
            default:
                break;
        }

        outputDevService.saveOutputDevice(dev);

        return ResponseEntity.ok(dev);
    }

    @Operation(summary = "Associate a device to a room", description = "Associate an output device to a existing room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a OK string"),
        @ApiResponse(responseCode = "422", description = "An output device with this ID does not exist!",  content = @Content),
        @ApiResponse(responseCode = "422", description = "A room with the specified ID does not exist!",  content = @Content)
    })
    @PostMapping("/associate") // associate a device to a room
    public @ResponseBody String associateDevice(@RequestParam String deviceID, @RequestParam String roomID) {
        OutputDevice device;
        Room room;

        try {
            // Check if a User with this ID exists
            device = outputDevService.findByID(deviceID);
            room = roomService.findByID(roomID);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (device == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An output device with this ID does not exist!");
        }

        if (room == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "A room with the specified ID does not exist!");
        }

        Room prevRoom = roomService.findByID(device.getDevice_location());

        if (prevRoom != null) {
            prevRoom.getDevices().remove(deviceID);
            roomService.saveRoom(prevRoom);
        }

        device.setDevice_location(roomID);
        outputDevService.saveOutputDevice(device);

        if (!room.getDevices().contains(deviceID)) { // dont add repeated devices
            room.addDevice(deviceID);
        }

        roomService.saveRoom(room);

        return "Saved";
    }

    @Operation(summary = "View a output device", description = "Get all the known information about a specific output device")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a Output Device instance"),
        @ApiResponse(responseCode = "422", description = "An output device with this ID does not exist!",  content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal processing error!",  content = @Content)
    })
    @GetMapping("/view")
    public @ResponseBody String viewOutputDevice(@RequestParam String id) {
        OutputDevice device;
        // Get the output device
        try {
            device = outputDevService.findByID(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (device == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "An output device with this ID does not exist!");
        }

        // Generate the output user object for the frontend
        JSONObject out = new JSONObject();
        out.put("name", device.getName());
        out.put("id", device.getID());
        out.put("category", OutputDeviceType.valueOf(device.getDevice_category().name()));
        out.put("location", device.getDevice_location());
        out.put("state", device.getCurrent_state());
        out.put("laststatechange", device.getLaststatechange());

        switch (device.getDevice_category()) {
            case AIR_CONDITIONER:
                out.put("temperature", device.getTemperature());
                break;
            case TELEVISION:
                out.put("channel", device.getCurrent_channel());
                out.put("volume", device.getSlider_value());
                break;
            case SPEAKER:
                out.put("music", device.getCurrent_music());
                out.put("volume", device.getSlider_value());
                break;
            case LIGHT:
                out.put("color", device.getColor());
                out.put("brightness", device.getSlider_value());
            default:
                break;
        }

        return out.toString();
    }

    // Change the state of an output device
    @Operation(summary = "Change the state of a device", description = "Alter the current state of a output device to a new one")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a Output Device instance"),
        @ApiResponse(responseCode = "422", description = "An output device with this ID does not exist!",  content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal processing error!",  content = @Content)
    })
    @PostMapping("/changeState")
    public String changeState(@RequestParam String id, @RequestBody String request_body) {
        OutputDevice device;
        // Get the output device
        try {
            device = outputDevService.findByID(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (device == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "An output device with this ID does not exist!");
        }

        JSONObject body = new JSONObject(request_body);

        if (body.has("state")) { // alteração de state
            device.setCurrent_state(body.getString("state"));
        }

        if (body.has("temperature") && device.getDevice_category() == OutputDeviceType.AIR_CONDITIONER) { // alteração
                                                                                                          // de
                                                                                                          // temperature
            device.setTemperature(body.getInt("temperature"));
        }

        if (body.has("channel") && device.getDevice_category() == OutputDeviceType.TELEVISION) { // alteração de channel
            device.setCurrent_channel(body.getString("channel"));
        }

        if (body.has("music") && device.getDevice_category() == OutputDeviceType.SPEAKER) { // alteração de music
            device.setCurrent_music(body.getString("music"));
        }

        if (body.has("color") && device.getDevice_category() == OutputDeviceType.LIGHT) { // alteração de color
            device.setColor(body.getString("color"));
        }

        device.setSlider_value("0");

        device.setLaststatechange(System.currentTimeMillis());

        outputDevService.saveOutputDevice(device);

        // Generate the output user object for the frontend
        JSONObject out = new JSONObject();

        out.put("name", device.getName());
        out.put("id", device.getID());
        out.put("category", OutputDeviceType.valueOf(device.getDevice_category().name()));
        out.put("location", device.getDevice_location());
        out.put("state", device.getCurrent_state());

        if (device.getDevice_category() == OutputDeviceType.AIR_CONDITIONER) {
            out.put("temperature", device.getTemperature());
        } else if (device.getDevice_category() == OutputDeviceType.TELEVISION) {
            out.put("channel", device.getCurrent_channel());
            out.put("volume", device.getSlider_value());
        } else if (device.getDevice_category() == OutputDeviceType.SPEAKER) {
            out.put("music", device.getCurrent_music());
            out.put("volume", device.getSlider_value());
        } else if (device.getDevice_category() == OutputDeviceType.LIGHT) {
            out.put("color", device.getColor());
            out.put("brightness", device.getSlider_value());
        }

        return out.toString();
    }

    // Get a full list of all the output devices
    @Operation(summary = "List all devices", description = "List all the known output devices")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a list of Output Device instances"),
        @ApiResponse(responseCode = "500", description = "Internal processing error!",  content = @Content)
    })
    @GetMapping("/list")
    public @ResponseBody List <OutputDevice> getOutputs() {
        LinkedList<OutputDevice> sources;

        // Get the the output devices list
        try {
            sources = outputDevService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        return sources;
    }

    // Get a full list of all the output devices
    @Operation(summary = "List all categories", description = "List all the categories of output devices")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a list of Output Device Categories")
    })
    @GetMapping("/listCategories")
    public @ResponseBody String getCategories() {
        List<JSONObject> output = new LinkedList<JSONObject>();

        for (OutputDeviceType type : OutputDeviceType.values()) {
            // Generate the output user object for the frontend
            JSONObject out = new JSONObject();

            out.put("id", type.toString());
            out.put("name", type.name());

            output.add(out);
        }

        return output.toString();
    }


    @Operation (summary = "Get the room of a device", description = "Get the room where a device is located")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns the name of the room"),
        @ApiResponse(responseCode = "422", description = "An output device with this ID does not exist!",  content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal processing error!",  content = @Content)
    })

    @GetMapping("/getRoom")
    public @ResponseBody String getRoom(@RequestParam String id) {
        OutputDevice device;
        // Get the output device
        try {
            device = outputDevService.findByID(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (device == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "An output device with this ID does not exist!");
        }

        String roomID = device.getDevice_location();

        if (roomID.equals("None")){ // no room associated
            return null;
        }

        Room room;

        try {
            room = roomService.findByID(roomID);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (room == null) {
            return null;
        }
        else {
            return room.getName();
        }



       
    }
}
