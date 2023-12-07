package mysweethome.MSHbackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.faces.annotation.RequestParameterMap;
import javax.crypto.SecretKeyFactory;

@Controller
@CrossOrigin("*")
@RestController
@RequestMapping(path = "/user")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String email, @RequestParam String password) {
        System.out.println("Adding user: " + firstname + " " + lastname + " " + email + " " + password);
        User usr = userService.findByEmail(email);

        if (usr != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "This email is already associated with another account!");
        }
        
        usr = new User();
        
        usr.setFirstName(firstname);
        usr.setLastName(lastname);
        usr.setEmail(email);


       try {
            Encoder encoder = Base64.getUrlEncoder().withoutPadding();

            //  Generate the random number
            SecureRandom random = new SecureRandom();

            //  Generate the salt
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            String saltStr = encoder.encodeToString(salt);

            //  Generate the salted key
            KeySpec spec = new PBEKeySpec(password.toCharArray(), saltStr.getBytes(), 65536, 128);

            //  Generate the final hashed + salted key
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            usr.setSalt(saltStr);
            usr.setPassword(encoder.encodeToString(hash));

            // Generate the token
            SecureRandom rng = new SecureRandom();
            byte bytes[] = new byte[64];
            rng.nextBytes(bytes);
            String token = encoder.encodeToString(bytes);
    
            usr.setActive_Token(token);
            userService.saveUser(usr);
            return ResponseEntity.ok(usr);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }
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
        System.out.println("Logging in user: " + email + " " + password);
        User user;

        // Check if a User with this login information exists or nor
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }
    
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User email has no account associated!");
        }
    
        String hashedPassword = user.getPassword();
        String hashedPassToCheck = "";
    
        Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        KeySpec spec = new PBEKeySpec(password.toCharArray(), user.getSalt().getBytes(), 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            hashedPassToCheck = encoder.encodeToString(hash);
        } 
        catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }
        
        if (!hashedPassword.equals(hashedPassToCheck)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User authentication is incorrect >");
        }
        
        
        SecureRandom rng = new SecureRandom();
        byte bytes[] = new byte[64];
        rng.nextBytes(bytes);

        user.setActive_Token(encoder.encodeToString(bytes));
        userService.saveUser(user);
            
        // Generate the output user object for the frontend
        JSONObject out = new JSONObject();
        out.put("id", user.getUid());
        out.put("firstname", user.getFirstName());
        out.put("lastname", user.getLastName());
        out.put("email", user.getEmail());
        out.put("token", user.getActive_Token());

        return out.toString(1);
    }

    @GetMapping(path = "/checkToken")
    public @ResponseBody String checkToken(@RequestParam String email, @RequestParam String token) {
        User user;

        // Check if a User with this login information exists or nor
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }
    
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User email has no account associated!");
        }

        if (!token.equals(user.getActive_Token())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The given token is not active for this user!");
        }
        return "OK";
    }

    //  Edit User information
    @GetMapping(path = "/edit")
    public @ResponseBody String editUser(@RequestParam String email, @RequestParam String password) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service has not been implemented yet!");
    }
}
