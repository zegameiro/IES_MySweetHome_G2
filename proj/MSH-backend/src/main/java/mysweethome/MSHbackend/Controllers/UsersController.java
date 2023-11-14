package mysweethome.MSHbackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;


@RestController
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        User usr = new User();
        
        usr.setName(name);
        usr.setEmail(email);
        usr.setPassword(password);
        
        userService.saveUser(usr);
        return ResponseEntity.ok(usr);
    }

    // View all information of a specific object based on ID
    @GetMapping(path = "/view")
    public @ResponseBody String viewUser(@RequestParam String id) {
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
        
        // Generate the output user object for the frontend
        JSONObject out = new JSONObject();
        out.put("id", user.getUid());
        out.put("name", user.getName());
        out.put("email", user.getEmail());

        return out.toString(1);
    }

    // View all information of a specific object based on ID
    @GetMapping(path = "/login")
    public @ResponseBody String loginUser(@RequestParam String email, @RequestParam String password) {
        User user;

        // Check if a User with this email and password exists
        try {
            user = userService.findByEmailAndPassword(email, password);
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (user == null) {
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An account with this credentials does not exist!");
        }
        
        // Generate the output user object for the frontend
        JSONObject out = new JSONObject();
        out.put("id", user.getUid());
        out.put("name", user.getName());
        out.put("email", user.getEmail());

        return out.toString(1);
    }

    // View all information of a specific object based on ID
    @GetMapping(path = "/view")
    public @ResponseBody ResponseEntity<User> updateUser(@RequestParam String id, @RequestParam String username) {
        User usr;

        // Check if a User with this ID exists
        try {
            usr = userService.findByID(id);
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (usr == null) {
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An user with the specified ID does not exist!");
        }
        
        usr.setName(username);
        userService.saveUser(usr);

        return ResponseEntity.ok(usr);
    }


    // View all information of a specific object based on ID
    @GetMapping(path = "/view")
    public @ResponseBody ResponseEntity<User> addDislike(@RequestParam String id, @RequestParam String dislike) {
        User usr;

        // Check if a User with this ID exists
        try {
            usr = userService.findByID(id);
        } 
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }

        if (usr == null) {
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An user with the specified ID does not exist!");
        }
        
        usr.addDislike(dislike);
        userService.saveUser(usr);

        return ResponseEntity.ok(usr);
    }

    
}
