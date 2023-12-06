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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping(path = "/routines")
@Tag(name = "Routine and Action Endpoints")
public class RoutinesController {

    @Autowired
    private ActionsService actionService;

    @Autowired
    private OutputDeviceRepository outputDeviceRepository;

    @Operation(summary = "Add a new action", description = "Add a new action to a specific output device")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a OK string"), 
        @ApiResponse(responseCode = "422", description = "An output device with the specified ID does not exist!",  content = @Content),
        @ApiResponse(responseCode = "422", description = "The specified action is not supported!",  content = @Content),
        @ApiResponse(responseCode = "422", description = "The specified action is not applicable to the specified output device!",  content = @Content)
    })
    @PostMapping("/add")
    public String addRoutine(Action action)
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

        return "Saved";

    }
    
}
