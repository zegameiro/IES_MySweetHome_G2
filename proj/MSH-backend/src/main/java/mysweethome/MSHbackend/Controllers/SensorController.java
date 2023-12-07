package mysweethome.MSHbackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;


import org.springframework.http.HttpStatus;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;
import java.util.LinkedList;

@RestController
@RequestMapping(path = "/sources")
@CrossOrigin("*")
@Tag(name = "Input Sensor and Datasource Management Endpoints")
public class SensorController {

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private RoomService roomService;

    // Get a full list of all the data sources (sensors)
    @Operation(summary = "Associate a device", description = "Register a device inside a specific room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a OK String"), 
        @ApiResponse(responseCode = "422", description = "Error! A sensor with this ID was not found!",  content = @Content),
        @ApiResponse(responseCode = "422", description = "Error! A room with this ID was not found!",  content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal processing error!",  content = @Content)
    })
    @PostMapping("/associate")
    public @ResponseBody String associateSource(@RequestParam String roomID , @RequestParam String sensor_id ) {
        // Get the sources list

        DataSource source;
        Room room;
        try {
            source = dataSourceService.findByID(sensor_id);
            room = roomService.findByID(roomID);
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (source == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error! A sensor with this ID was not found!");
        }
        if (room == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error! A room with this ID was not found!");
        }

        room.addDevice(sensor_id);

        roomService.saveRoom(room);

        source.setLocation(roomID);
        dataSourceService.saveDataSource(source);

        return "Saved";
    }


    // View all information of a specific object based on ID
    @Operation(summary = "View a device instance", description = "View a input device's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns Input Device instance"), 
        @ApiResponse(responseCode = "422", description = "A sensor with the specified ID does not exist!",  content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal processing error!",  content = @Content)
    })
    @GetMapping("/view")
    public @ResponseBody ResponseEntity<DataSource> viewSource(@RequestParam String id) {
        DataSource source;

        // Check if a User with this ID exists
        try {
            source = dataSourceService.findByID(id);
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (source == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "A sensor with the specified ID does not exist!");
        }

        return ResponseEntity.ok(source);
    }
    
    @Operation(summary = "List device IDs", description = "List all device IDs available")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a list of all device IDs"), 
        @ApiResponse(responseCode = "500", description = "Internal processing error!",  content = @Content)
    })
    @GetMapping("/list/ids")
    public @ResponseBody ResponseEntity<List<String>> getIds(){
        List<DataSource> sources;
        List<String> sourceIDs = new LinkedList<String>();

        // Get the sources list
        try {
            sources = dataSourceService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        for (DataSource src : sources) {
            sourceIDs.add(src.getID());
        }

        return ResponseEntity.ok(sourceIDs);
    }

    @Operation(summary = "List devices", description = "List all devices available")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a list of all devices"), 
        @ApiResponse(responseCode = "500", description = "Internal processing error!",  content = @Content)
    })
    @GetMapping("/list")
    public @ResponseBody ResponseEntity<List<DataSource>> getSources() {
        LinkedList<DataSource> sources;

        // Get the sources list
        try {
            sources = dataSourceService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        return ResponseEntity.ok(sources);
    }
}
