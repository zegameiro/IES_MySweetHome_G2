package mysweethome.MSHbackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.LinkedList;

@RestController
@RequestMapping(path = "/outputs")
public class OutputDeviceController {

    @Autowired
    private OutputDeviceService outputDevService;
    
    @Autowired
    private RoomService roomService;

    @PostMapping("/add")
    public ResponseEntity<OutputDevice> addOutputDevice(@RequestParam String name , @RequestParam String state, @RequestParam int category, @RequestParam String roomID) {
        OutputDevice dev = new OutputDevice();

        Room room;
        try {
            //  Check if a User with this ID exists
            room = roomService.findByID(roomID);
        }
        catch ( Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (room == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "A room with the specified ID does not exist!");
        }
        
        OutputDeviceType dev_category = OutputDeviceType.valueOf(category);
        dev.setName(name);
        dev.setCurrent_state(state);
        dev.setDevice_category(dev_category);
        dev.setDevice_location(room.getUid());

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
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid device category!");
        }


        
        String deviceID = outputDevService.saveGetOutputDevice(dev).getID();

        room.addDevice(deviceID);
        roomService.saveRoom(room);

        return ResponseEntity.ok(dev);
    }

    @GetMapping("/view")
    public @ResponseBody String viewOutputDevice(@RequestParam String id) {
        OutputDevice device;
        //  Get the output device
        try {
            device = outputDevService.findByID(id);
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (device == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An output device with this ID does not exist!");
        }

        // Generate the output user object for the frontend
        JSONObject out = new JSONObject();
        out.put("name", device.getName());
        out.put("id", device.getID());
        out.put("category", OutputDeviceType.valueOf(device.getDevice_category().name()));
        out.put("location", device.getDevice_location());
        out.put("state", device.getCurrent_state());

        switch (device.getDevice_category()) {
            case AIR_CONDITIONER:
                out.put("temperature", device.getTemperature());
                break;
            case TELEVISION:
                out.put("channel", device.getCurrent_channel());
                break;
            case SPEAKER:
                out.put("music", device.getCurrent_music());
                break;
            case LIGHT:
                out.put("color", device.getColor());
            default:
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid device category!");
        }

        return out.toString();
    }


     
    //  Change the state of an output device
    @PostMapping("/changeState")
    public String changeState(@RequestParam String id, @RequestBody String request_body) {
        OutputDevice device;
        //  Get the output device
        try {
            device = outputDevService.findByID(id);
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (device == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An output device with this ID does not exist!");
        }

        JSONObject body = new JSONObject(request_body);

        if (body.has("state") ) { // alteração de state
            device.setCurrent_state(body.getString("state"));
        }

        if (body.has("temperature") && device.getDevice_category() == OutputDeviceType.AIR_CONDITIONER) { // alteração de temperature
            device.setTemperature(body.getInt("temperature"));
        }

        if (body.has("channel") && device.getDevice_category() == OutputDeviceType.TELEVISION ) { // alteração de channel
            device.setCurrent_channel(body.getString("channel"));
        }

        if (body.has("music") && device.getDevice_category() == OutputDeviceType.SPEAKER) { // alteração de music
            device.setCurrent_music(body.getString("music"));
        }

        if (body.has("color") && device.getDevice_category() == OutputDeviceType.LIGHT) { // alteração de color
            device.setColor(body.getString("color"));
        }


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
        }
        else if (device.getDevice_category() == OutputDeviceType.TELEVISION) {
            out.put("channel", device.getCurrent_channel());
        }
        else if (device.getDevice_category() == OutputDeviceType.SPEAKER) {
            out.put("music", device.getCurrent_music());
        }


        return out.toString();
    }
    

    //  Get a full list of all the output devices
    @GetMapping("/list")
    public @ResponseBody String getOutputs() {
        LinkedList<OutputDevice> sources;
        List<JSONObject> output = new LinkedList<JSONObject>();

        //  Get the the output devices list
        try {
            sources = outputDevService.getAll();
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }            

        for (OutputDevice src : sources) {
            // Generate the output user object for the frontend
            JSONObject out = new JSONObject();
            OutputDeviceType dev_category = src.getDevice_category();
            
            out.put("name", src.getName());
            out.put("id", src.getID());
            out.put("category", dev_category.toString());
            out.put("location", src.getDevice_location());
            out.put("state", src.getCurrent_state());

            switch (dev_category) {
                case AIR_CONDITIONER:
                    out.put("temperature", src.getTemperature());
                    break;
                case TELEVISION:
                    out.put("channel", src.getCurrent_channel());
                    break;
                case SPEAKER:
                    out.put("music", src.getCurrent_music());
                    break;
                case LIGHT:
                    break; // nao há opçao nenhuma por enqnt
                default:
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid device category!");
            }
            
    
            output.add(out);
        }

        return output.toString();
    }

    //  Get a full list of all the output devices
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
}
