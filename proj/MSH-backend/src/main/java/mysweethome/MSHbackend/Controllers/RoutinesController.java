package mysweethome.MSHbackend.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Repositories.OutputDeviceRepository;
import mysweethome.MSHbackend.Repositories.SBRepo;
import mysweethome.MSHbackend.Repositories.TBRepo;
import mysweethome.MSHbackend.Services.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/routines")
@Tag(name = "Routine and Action Endpoints")
public class RoutinesController {

    @Autowired
    private RoutineService routines;

    @Autowired
    private SBRepo sbRepo;

    @Autowired
    private TBRepo tbRepo;

    @Autowired
    private ActionsService actions;

    @Autowired
    private OutputDeviceRepository outputDeviceRepository;

    @Operation(summary = "Add a new Routine", description = "Add a new routine to the list of possible routines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a OK string"),
            @ApiResponse(responseCode = "422", description = "An action with the specified ID does not exist!", content = @Content),
            @ApiResponse(responseCode = "422", description = "An output device with the specified ID does not exist!", content = @Content),
            @ApiResponse(responseCode = "422", description = "The specified action is not applicable to the specified output device!", content = @Content)
    })

    @PostMapping("/addTB")
    public @ResponseBody String addRoutineTB(@RequestBody TimeBasedRoutine routine) {

        routine.setId();

        Action act = routine.getAssociated_action();

        if (act == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "An action with the specified ID does not exist!");
        }

        act.setId();

        System.out.println("Action ID is: " + act.getId());

        String associated_device_id = act.getOutputDeviceID(); // device associated to this action

        if (outputDeviceRepository.findById(associated_device_id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "An output device with the specified ID does not exist!");
        }

        String action_description = act.getAction_description();

        OutputDevice device = outputDeviceRepository.findById(associated_device_id).get();

        if (device == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Output device not found!");
        }

        // check if action is supported by this device
        if (!device.getDevice_category().getPossibleActions().contains(action_description)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "The specified action is not applicable to the specified output device!");
        }

        act.setTimestamp(System.currentTimeMillis());

        routines.saveTBRoutine(routine);
        actions.saveAction(act);

        return "OK";

    }

    @PostMapping("/addSB")
    public @ResponseBody String addRoutineSB(@RequestBody SensorBasedRoutine routine) {

        routine.setId();

        Action act = routine.getAssociated_action();

        if (act == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "An action with the specified ID does not exist!");
        }

        act.setId();

        String associated_device_id = act.getOutputDeviceID(); // device associated to this action

        if (outputDeviceRepository.findById(associated_device_id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "An output device with the specified ID does not exist!");
        }

        String action_description = act.getAction_description();

        OutputDevice device = outputDeviceRepository.findById(associated_device_id).get();

        if (device == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Output device not found!");
        }

        // check if action is supported by this device
        if (!device.getDevice_category().getPossibleActions().contains(action_description)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "The specified action is not applicable to the specified output device!");
        }

        act.setTimestamp(System.currentTimeMillis());

        routines.saveSBRoutine(routine);
        actions.saveAction(act);

        return "OK";

    }

    @Operation(summary = "Get all valid actions", description = "Get a list of all valid actions for a gicen device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a list of action strings"),
            @ApiResponse(responseCode = "422", description = "An output device with the specified ID does not exist!", content = @Content)
    })
    @GetMapping("/check")
    public @ResponseBody List<String> checkActions(@RequestParam String device_id) {

        OutputDevice device = outputDeviceRepository.findById(device_id).get();

        if (device == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "An output device with the specified ID does not exist!");
        }

        return device.getDevice_category().getPossibleActions();

    }

    @Operation(summary = "List all Time Based routines", description = "List all the routines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a list of all routines"),
            @ApiResponse(responseCode = "422", description = "No routines found!", content = @Content)
    })
    @GetMapping("/listTB")
    public @ResponseBody ResponseEntity<List<TimeBasedRoutine>> listTB() {
        List<TimeBasedRoutine> routines_list = routines.findAllTB();

        if (routines_list == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "No time based routines found!");
        }

        return ResponseEntity.ok(routines_list);

    }

    @Operation(summary = "List all Sensor Based routines", description = "List all the routines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a list of all routines"),
            @ApiResponse(responseCode = "422", description = "No routines found!", content = @Content)
    })
    @GetMapping("/listSB")
    public @ResponseBody ResponseEntity<List<SensorBasedRoutine>> listSB() {
        List<SensorBasedRoutine> routines_list = routines.findAllSB();

        if (routines_list == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "No sensor based routines found!");
        }

        return ResponseEntity.ok(routines_list);

    }



    @Operation(summary = "Change the state ( active or not ) of a time based routine", description = "Change the state of a time based routine from ON to OFF and vice-versa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a OK string"),
            @ApiResponse(responseCode = "422", description = "No time based routine with this ID was found!", content = @Content)
    })
    @PostMapping("/changeStateTB")
    public @ResponseBody String changeStateTB(@RequestParam String id) {

        TimeBasedRoutine routine = routines.findTBByID(id);

        if (routine == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "No time based routine with this ID was found!");
        }

        routine.setActive(!routine.isActive());

        routines.saveTBRoutine(routine);

        return "OK";

    }

    @Operation(summary = "Change the state ( active or not ) of a sensor based routine", description = "Change the state of a sensor based routine from ON to OFF and vice-versa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a OK string"),
            @ApiResponse(responseCode = "422", description = "No time based routine with this ID was found!", content = @Content)
    })
    @PostMapping("/changeStateSB")
    public @ResponseBody String changeStateSB(@RequestParam String id) {

        SensorBasedRoutine routine = routines.findSBByID(id);

        if (routine == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "No sensor based routine with this ID was found!");
        }

        routine.setActive(!routine.isActive());

        routines.saveSBRoutine(routine);

        return "OK";

    }

    @Operation (summary = "Delete a time based routine", description = "Delete a time based routine from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a OK string"),
            @ApiResponse(responseCode = "422", description = "No time based routine with this ID was found!", content = @Content)
    })
    @DeleteMapping("/deleteTB")
    public @ResponseBody String deleteTB(@RequestParam String id) {

        TimeBasedRoutine routine = routines.findTBByID(id);

        if (routine == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "No time based routine with this ID was found!");
        }

        routines.deleteTB(routine);

        return "OK";

    }

    @Operation (summary = "Delete a sensor based routine", description = "Delete a sensor based routine from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a OK string"),
            @ApiResponse(responseCode = "422", description = "No sensor based routine with this ID was found!", content = @Content)
    })
    @DeleteMapping("/deleteSB")
    public @ResponseBody String deleteSB(@RequestParam String id) {

        SensorBasedRoutine routine = routines.findSBByID(id);

        if (routine == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "No sensor based routine with this ID was found!");
        }

        routines.deleteSB(routine);

        return "OK";

    }

}