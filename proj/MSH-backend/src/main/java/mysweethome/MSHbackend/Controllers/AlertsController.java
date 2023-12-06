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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/alerts")
@Tag(name = "Alert Management Endpoints")
public class AlertsController {

    @Autowired
    private AlertService alertService;

    // View all information of a specific object based on ID
    @Operation(summary = "View alerts information", description = "Retrieve all the information about a specific alert")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns alert instance"),
        @ApiResponse(responseCode = "422", description = "An alert object with the specified ID does not exist!", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal processing error!", content = @Content)
    })
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
    
    @Operation(summary = "List active alerts", description = "Retrieve all the current active (unread) alerts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns list alert instances"),
        @ApiResponse(responseCode = "500", description = "Internal processing error!", content = @Content)
    })
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

    @Operation(summary = "List all alerts", description = "Retrieve all the ongoing and past alerts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns list alert instances"),
        @ApiResponse(responseCode = "500", description = "Internal processing error!", content = @Content)
    })
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

    @Operation(summary = "Mark an alert as read", description = "Mark a specific alert as read")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a OK String"),
        @ApiResponse(responseCode = "422", description = "An alert object with the specified ID does not exist!", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal processing error!", content = @Content)
    })
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

        return "OK";
    }
}
