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

    //  Get a full list of all the data sources (sensors)
    @GetMapping("/list")
    public @ResponseBody String getSources() {
        LinkedList<OutputDevice> sources;
        List<JSONObject> output = new LinkedList<JSONObject>();

        //  Get the sources list
        try {
            sources = outputDevService.getAll();
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        for (OutputDevice src : sources) {
            // Generate the output user object for the frontend
            JSONObject out = new JSONObject();
            
            out.put("id", src.getDevice_id());
            out.put("category", src.getDevice_category());
            out.put("location", src.getDevice_location());
            out.put("state", src.getCurrent_state());
    
            output.add(out);
        }

        return output.toString();
    }
}
