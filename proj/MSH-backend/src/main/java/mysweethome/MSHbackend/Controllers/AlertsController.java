package mysweethome.MSHbackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;


import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.LinkedList;

@RestController
@RequestMapping(path = "/alerts")
public class AlertsController {

    @Autowired
    private AlertService alertService;


    // View all information of a specific object based on ID
    @GetMapping("/view")
    public @ResponseBody String viewSource(@RequestParam String id) {
        Alert alert;

        // Check if a alert with this ID exists
        try {
            alert = alertService.findByID(id);
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (alert == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An alert object with the specified ID does not exist!");
        }
        
        // Generate the output user object for the frontend
        JSONObject out = new JSONObject();
        out.put("id", alert.getId());
        out.put("timestamp", alert.getTimestamp());
        out.put("alert_information", alert.getAlert_information());

        return out.toString(1);
    }
    
    @GetMapping("/list")
    public @ResponseBody String getSources() {
        LinkedList<Alert> alerts;
        List<JSONObject> output = new LinkedList<JSONObject>();

        // Get the sources list
        try {
            alerts = alertService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        for (Alert alert : alerts) {
            // Generate the output user object for the frontend
            JSONObject out = new JSONObject();

            out.put("id", alert.getId());
            out.put("timestamp", alert.getTimestamp());
            out.put("alert_information", alert.getAlert_information());

            output.add(out);
        }

        return output.toString();
    }
}
