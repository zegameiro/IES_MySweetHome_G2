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


@RestController
@RequestMapping(path = "/user")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String email, @RequestParam String password) {
        User usr = userService.findByEmail(email);

        if (usr != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An user with the specified ID does not exist!");
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
    @GetMapping("/view")
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
        out.put("firstname", user.getFirstName());
        out.put("lastname", user.getLastName());
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
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An account with these credentials does not exist!");
        }
        
        // Generate the output user object for the frontend
        JSONObject out = new JSONObject();
        out.put("id", user.getUid());
        out.put("firstname", user.getFirstName());
        out.put("lastname", user.getLastName());
        out.put("email", user.getEmail());

        return out.toString(1);
    }

    // View all information of a specific object based on ID
    // @GetMapping(path = "/view")
    // public @ResponseBody ResponseEntity<User> addDislike(@RequestParam String id, @RequestParam String dislike) {
    //     User usr;

    //     // Check if a User with this ID exists
    //     try {
    //         usr = userService.findByID(id);
    //     } 
    //     catch (Exception e) {
    //         throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
    //     }

    //     if (usr == null) {
    //     throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An user with the specified ID does not exist!");
    //     }
        
    //     usr.addDislike(dislike);
    //     userService.saveUser(usr);

    //     return ResponseEntity.ok(usr);
    // }

    
}
