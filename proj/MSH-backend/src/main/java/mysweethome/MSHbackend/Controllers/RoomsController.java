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

import java.util.List;
import java.util.LinkedList;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;


@RestController
@RequestMapping(path = "/room")
public class RoomsController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private DataSourceService inpService;
    @Autowired
    private OutputDeviceService outService;

    //  Create a new room
    @PostMapping("/add")
    public ResponseEntity<Room> addRoom(@RequestParam String name, @RequestParam Integer floornumber) {
        Room room = new Room();
        
        room.setName(name);
        room.setFloorNumber(floornumber);
        
        roomService.saveRoom(room);
        return ResponseEntity.ok(room);
    }

    //  View all information of a specific object based on ID
    @GetMapping("/view")
    public @ResponseBody String viewRoom(@RequestParam String id) {
        Room room;

        //  Check if a Room with this ID exists
        try {
            room = roomService.findByID(id);
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (room == null) {
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An room with the specified ID does not exist!");
        }
        
        //  Generate the output room object for the frontend
        JSONObject out = new JSONObject();
        out.put("id", room.getUid());
        out.put("name", room.getName());
        out.put("floornumber", room.getFloorNumber());
        out.put("devices", room.getDevices());

        return out.toString(1);
    }

    //  List all rooms
    @GetMapping("/list")
    public @ResponseBody String listRoom() {
        List<Room> rooms;   
        List<JSONObject> output = new LinkedList<JSONObject>();

        //  Get the rooms list
        try {
            rooms = roomService.findAllRoom();
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        for (Room room : rooms) {
            // Generate the output user object for the frontend
            JSONObject out = new JSONObject();
            
            out.put("id", room.getUid());
            out.put("name", room.getName());
            out.put("floornumber", room.getFloorNumber());
            out.put("devices", room.getDevices());
    
            output.add(out);
        }

        return output.toString();   
    }

    //  View all information of a specific object based on ID
    @PostMapping(path = "/addDevice")
    public @ResponseBody String addDevice(@RequestParam String roomID, @RequestParam String deviceID) {
        Room room;
        DataSource inputDev = null;
        OutputDevice outputDev = null;

        //  Check if a User with this ID exists
            room = roomService.findByID(roomID);
            inputDev = inpService.findByID(deviceID);

            if (inputDev == null) {
                outputDev = outService.findByID(deviceID);
            }

        if (room == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An room with the specified ID does not exist!");
        }

        if (inputDev == null && outputDev == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "A device with the specified ID does not exist!");
        }
        
        room.addDevice(deviceID);
        roomService.saveRoom(room);

        return "Saved";
    }

    
}
