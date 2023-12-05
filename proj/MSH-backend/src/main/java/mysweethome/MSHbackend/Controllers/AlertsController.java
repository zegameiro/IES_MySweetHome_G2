package mysweethome.MSHbackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/alerts")
public class AlertsController {

    @Autowired
    private AlertService alertService;


    // View all information of a specific object based on ID
    @GetMapping("/view")
    public @ResponseBody Alert viewSource(@RequestParam String id) {
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
        
        return alert;
    }
    
    @GetMapping("/list")
    public @ResponseBody LinkedList<Alert> getUnreadAlerts() {
        LinkedList<Alert> alerts;

        // Get the sources list
        try {
            alerts = alertService.getAllUnread();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        return alerts;
    }

    @GetMapping("/list/all")
    public @ResponseBody LinkedList<Alert> getAll() {
        LinkedList<Alert> alerts;

        // Get the sources list
        try {
            alerts = alertService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        return alerts;
    }

    @PostMapping("/mark")
    public @ResponseBody String markAsRead(@RequestParam String id) {
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

        // Mark the alert as read
        try {
            alert.setMarked_as_read(true);
            alertService.saveAlert(alert);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        return "Sucessfully marked alert as read!";
    }
}
