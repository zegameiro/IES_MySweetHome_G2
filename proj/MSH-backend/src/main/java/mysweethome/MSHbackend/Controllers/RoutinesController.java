package mysweethome.MSHbackend.Controllers;

import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/routines")
public class RoutinesController {

    @Autowired
    private ActionsService actionsService;
    @Autowired
    private OutputDeviceService outputDevicesService;

    @GetMapping("/add")
    public void addRoutine(@RequestBody Action action) {
        boolean check_validity = true;
        OutputDevice invalid_device = null;
        ArrayList<OutputDevice> output_devices = action.getOutput_devices();


        /*
         * TO DO
         * 
         * Mapear a ação na request para a ação correspondente no enum
         * 
         */

        for (OutputDevice output_device : output_devices) {
            if (PossibleActions.TURN_ON.isApplicableTo(output_device.getDevice_category())) {
                check_validity = false;
                invalid_device = output_device;
                break;
            }
        }

        if (!check_validity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This action is not possible for device "
                    + invalid_device.getDevice_id() + " of type -> " + invalid_device.getDevice_category());
        }

        actionsService.addAction(action);



    }

    @GetMapping("/delete")
    public void deleteRoutine(int routine_id) {

    }

    @GetMapping("/update")
    public void updateRoutine(int routine_id, Action action) {

    }

    @GetMapping("/list")
    public void listRoutines() {

    }

}
