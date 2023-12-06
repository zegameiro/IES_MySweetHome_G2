package mysweethome.MSHbackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin("*")
@RestController
@RequestMapping(path = "/user")
@Tag(name = "User Management Endpoints")
public class UsersController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new user", description = "Create a new user account in the application")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User successfully created"), 
        @ApiResponse(responseCode = "422", description = "This email is already associated with another account!",  content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String email, @RequestParam String password) {
        System.out.println("ENTERED ADD USER");
        System.out.println("Adding user: " + firstname + " " + lastname + " " + email + " " + password);
        User usr = userService.findByEmail(email);

        if (usr != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "This email is already associated with another account!");
        }
        
        usr = new User();
        
        usr.setFirstName(firstname);
        usr.setLastName(lastname);
        usr.setEmail(email);
        usr.setPassword(password);
        
        userService.saveUser(usr);
        return ResponseEntity.ok(usr);
    }

    // View all information of a specific object based on ID
    @Operation(summary = "View a specific user", description = "Get the public information of a specific user based on the provided ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns the User instance"), 
        @ApiResponse(responseCode = "422", description = "An user with the specified ID does not exist!",  content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal processing error!",  content = @Content)
    })
    @GetMapping("/view")
    public @ResponseBody ResponseEntity<User> viewUser(@RequestParam String id) {
        User user;

        // Check if a User with this ID exists
        try {
            user = userService.findByID(id);
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (user == null) {
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An user with the specified ID does not exist!");
        }

        return ResponseEntity.ok(user);
    }

    // View all information of a specific object based on ID
    @Operation(summary = "Login into the application", description = "Login inside the application with the provided email and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns the User instance"), 
        @ApiResponse(responseCode = "422", description = "An account with these credentials does not exist!",  content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal processing error!",  content = @Content)
    })
    @GetMapping(path = "/login")
    public @ResponseBody ResponseEntity<User> loginUser(@RequestParam String email, @RequestParam String password) {
        User user;

        // Check if a User with this email and password exists
        try {
            user = userService.findByEmailAndPassword(email, password);
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (user == null) {
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An account with these credentials does not exist!");
        }
        
        return ResponseEntity.ok(user);
    }

    //  Edit User information
    @Operation(summary = "Edit a User's information", description = "This endpoint is still not needed and therefore, not yet given time to be implemented")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "503", description = "Service has not been implemented yet!",  content = @Content)
    })
    @PutMapping(path = "/edit")
    public @ResponseBody ResponseStatusException editUser(@RequestParam String email, @RequestParam String password) {
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service has not been implemented yet!");
    }
}
