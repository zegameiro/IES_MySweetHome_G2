package mysweethome.MSHbackend.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Repositories.OutputDeviceRepository;
import mysweethome.MSHbackend.Services.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/routines")
public class RoutinesController {

    @Autowired
    private ActionsService actionService;

    @Autowired
    private OutputDeviceRepository outputDeviceRepository;

    @PostMapping("/add")
    public void addRoutine(@RequestBody Routine routine) {

        System.out.println(routine.toString());

        Action act = routine.getAssociated_action();

        if (act == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Action not found.");
        }

        String associated_device_id = act.getOutputDeviceID(); // device associated to this action

        if (outputDeviceRepository.findById(associated_device_id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Output device not found.");
        }

        String action_description = act.getAction_description();

        OutputDevice device = outputDeviceRepository.findById(associated_device_id).get();

        if (device == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Output device not found.");
        }
        

        PossibleActions ola = PossibleActions.valueOf(device.getDevice_category().toString());

        if (ola == null) {
            System.out.println("cock");
        }
        else{
            System.out.println(ola.toString());
        }







    }

}
