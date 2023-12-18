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

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;

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
        
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Check if a given token is active for an account", description = "Check if a given token corresponds to the current active token of an account for authentication purposes.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns an OK String"), 
        @ApiResponse(responseCode = "422", description = "User email has no account associated!",  content = @Content),
        @ApiResponse(responseCode = "422", description = "The given token is not active for this user!",  content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal processing error!",  content = @Content)
    })
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
    @Operation(summary = "Edit a User's information", description = "This endpoint is still not needed and therefore, not yet given time to be implemented")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "503", description = "Service has not been implemented yet!",  content = @Content)
    })
    @PutMapping(path = "/edit")
    public @ResponseBody ResponseStatusException editUser(@RequestParam String email, @RequestParam String password) {
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service has not been implemented yet!");
    }
}
