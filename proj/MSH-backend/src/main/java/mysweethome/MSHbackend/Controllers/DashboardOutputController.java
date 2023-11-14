package mysweethome.MSHbackend.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import mysweethome.MSHbackend.Models.*;

@RestController(/*definir o endpoint*/)
public class DashboardOutputController {
  // Create and save a new app_user object to the repository (database)
  @GetMapping(path = "/getUser")
  public @ResponseBody String addapp_user(@RequestParam String name, @RequestParam String email,
                                            @RequestParam String password, @RequestParam String cartao) {

    
}





    // Check if any required value is empty
    if (name == null || name.equals("") || email == null || email.equals("")
        || password == null || password.equals("") || cartao == null || cartao.equals("") || role == null
        || role.equals("")) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Provide all the required data fields!");
    }

    // Check the given email is already associated with another user
    if (app_userRepository.findapp_userByEmail(email) != null) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "A user with this email already exists!");
    }

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
    Pattern pat = Pattern.compile(emailRegex); 
    // Check if the email is valid
    if (!pat.matcher(email).matches()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
          "The provided Email must be valid!");
    }

    String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+]).{8,}$";
    pat = Pattern.compile(passwordRegex); 
    // Check if the password is valid
    if (!pat.matcher(password).matches()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
          "The provided Password must have more than 8 characters, 1 lowercase, 1 uppercase, 1 number and 1 special character!");
    }

    // Check if the card number has 12 characters in lenght
    if (cartao.length() != 12) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
          "The Card number must have twelve digits!");
    }

    
    if (img != null) {
      String OGfileName = img.getOriginalFilename();
      String fileExtention = OGfileName.substring(OGfileName.lastIndexOf(".") + 1);
      String[] a= {"png", "jpeg", "jpg", "tiff", "tif", "webp"};

      //  Check file type
      if (!Arrays.asList(a).contains(fileExtention)) {
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
            "The image file must be of the png, jpeg, jpg, tiff, tif or webp type!");
      }
    
    }

    // Check the role is app_user or admin
    if (!role.equals("user") && !role.equals("admin")) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
          "The role value must either be 'user' or 'admin'!");
    }

    // Register the App_User Object
    try {
      App_User usr = new App_User();
      usr.setName(name);
      usr.setEmail(email);
      usr.setPassword(password);
      usr.setCredit_Card(cartao);
      usr.setRole(role);

      String folder = "../../frontendSecure/src/assets/prod_images/";
      String filename = usr.getName().replace("\s", "") + ".jpg";

      Path path = Paths.get(folder + filename);

      if (img != null) {
        // Create the directory if it does not exist
        if (!Files.exists(path.getParent())) {
          Files.createDirectories(path.getParent());
        }

        // Create the file if it does not exist
        if (!Files.exists(path)) {
          Files.createFile(path);
        }
        try (InputStream inputStream = img.getInputStream()) {
          Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
          throw new IOException("Could not save image file: " + filename, e);
        }

        usr.setImage("/src/assets/prod_images/" + filename);
      } else {
        usr.setImage("");
      }

      Encoder encoder = Base64.getUrlEncoder().withoutPadding();

      //  Generate the random number
      SecureRandom random = new SecureRandom();
      //  Generate the salt
      byte[] salt = new byte[16];
      random.nextBytes(salt);
      //  Generate the salted key
      KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
      //  Generate the final hashed + salted key
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      byte[] hash = factory.generateSecret(spec).getEncoded();

      usr.setSalt(encoder.encodeToString(salt));
      usr.setPassword(encoder.encodeToString(hash));

      // Generate the token
      SecureRandom rng = new SecureRandom();
      byte bytes[] = new byte[64];
      rng.nextBytes(bytes);
      String token = encoder.encodeToString(bytes);

      usr.setActive_Token(token);
      app_userRepository.save(usr);

      // Generate the output user object for the frontend
      JSONObject out = new JSONObject();
      out.put("id", usr.getID().toString());
      out.put("name", usr.getName());
      out.put("email", usr.getEmail());
      out.put("image", usr.getImage());
      out.put("token", usr.getActive_Token());
      out.put("shopping_Cart", usr.getShopping_Cart());
      out.put("request_History", usr.getRequest_History());

      return out.toString(1);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
    }
  }
