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

import java.util.List;

import org.springframework.http.HttpStatus;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@CrossOrigin("*")
@RequestMapping(path = "/room")
@Tag(name = "Room Management Endpoints")
public class RoomsController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private DataSourceService inpService;
    @Autowired
    private OutputDeviceService outService;

    //  Create a new room
    @Operation(summary = "Add a new room", description = "Add a new room to our application in a specific floor number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a Room instance"), 
        @ApiResponse(responseCode = "422", description = "A room with this name already exists!",  content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<Room> addRoom(@RequestParam String name, @RequestParam Integer floornumber, @RequestParam String type) {
        Room room = roomService.findByName(name);

        if (room != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "A room with this name already exists!");
        }

        room = new Room();

        room.setName(name);
        room.setFloornumber(floornumber);
        room.setType(type);
        
        roomService.saveRoom(room);
        return ResponseEntity.ok(room);
    }

    //  View all information of a specific object based on ID
    @Operation(summary = "View a room", description = "Retrieve all the information of a specific room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a Room instance"), 
        @ApiResponse(responseCode = "422", description = "An room with the specified ID does not exist!",  content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal processing error!",  content = @Content)
    })
    @GetMapping("/view")
    public @ResponseBody ResponseEntity<Room> viewRoom(@RequestParam String id) {
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
        
        return ResponseEntity.ok(room);
    }

    //  List all rooms
    @Operation(summary = "List all rooms", description = "Retrieve all the known Rooms")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a list of all rooms"), 
        @ApiResponse(responseCode = "500", description = "Internal processing error!",  content = @Content)
    })
    @GetMapping("/list")
    public @ResponseBody ResponseEntity<List<Room>> listRoom() {
        List<Room> rooms;

        //  Get the rooms list
        try {
            rooms = roomService.findAllRoom();
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        return ResponseEntity.ok(rooms);   
    }

    //  View all information of a specific object based on ID
    @Operation(summary = "Associate a device", description = "Associate a device to this room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a Ok string"), 
        @ApiResponse(responseCode = "422", description = "An room with the specified ID does not exist!",  content = @Content),
        @ApiResponse(responseCode = "422", description = "A device with the specified ID does not exist!",  content = @Content)
    })
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
