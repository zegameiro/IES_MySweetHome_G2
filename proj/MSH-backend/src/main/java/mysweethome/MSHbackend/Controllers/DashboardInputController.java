package mysweethome.MSHbackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;


@RestController
public class DashboardInputController {

    @Autowired
    private UserService userService;

    @PostMapping("/adduser")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getuser/{name}")
    public ResponseEntity<User> getUser(@PathVariable String name) {
        User user = userService.findByName(name);
        return ResponseEntity.ok(user);
    }




    // save user method




    
}
