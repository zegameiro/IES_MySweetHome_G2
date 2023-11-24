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



@RestController
@RequestMapping(path = "/routines")
public class RoutinesController {

    @Autowired
    private ActionsService actionService;

    @Autowired
    private OutputDeviceRepository outputDeviceRepository;

    @PostMapping("/add")
    public void addRoutine(Action action)
    {

        String device_id = action.getOutputDeviceID();

        if (outputDeviceRepository.findByID(device_id) == null)
        {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An output device with the specified ID does not exist!");
        }

        if (PossibleActions.valueOf(action.getAction_description()) == null)
        {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The specified action is not supported!");
        }

        if (PossibleActions.valueOf(action.getAction_description()).isApplicableTo(outputDeviceRepository.findByID(device_id).getDevice_category()) == false)
        {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The specified action is not applicable to the specified output device!");
        }

        actionService.addAction(action);

    }
    
}
