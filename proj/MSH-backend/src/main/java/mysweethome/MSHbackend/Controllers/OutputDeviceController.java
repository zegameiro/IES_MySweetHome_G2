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
    public ResponseEntity<OutputDevice> addOutputDevice(@RequestParam String state, @RequestParam int category, @RequestParam String roomID) {
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
        
        

        dev.setCurrent_state(state);
        dev.setDevice_category(OutputDeviceType.valueOf(category));
        dev.setDevice_location(room.getUid());
        
        String deviceID = outputDevService.saveGetOutputDevice(dev).getID();

        room.addDevice(deviceID);
        roomService.saveRoom(room);

        return ResponseEntity.ok(dev);
    }

    //  Change the state of an output device
    @PostMapping("/changeState")
    public @ResponseBody String changeState(@RequestParam String id, String newState) {
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

        device.setCurrent_state(newState);
        outputDevService.saveOutputDevice(device);

        // Generate the output user object for the frontend
        JSONObject out = new JSONObject();
        
        out.put("id", device.getID());
        out.put("category", OutputDeviceType.valueOf(device.getDevice_category().name()));
        out.put("location", device.getDevice_location());
        out.put("state", device.getCurrent_state());

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
            
            out.put("id", src.getID());
            out.put("category", OutputDeviceType.valueOf(src.getDevice_category().name()));
            out.put("location", src.getDevice_location());
            out.put("state", src.getCurrent_state());
    
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
