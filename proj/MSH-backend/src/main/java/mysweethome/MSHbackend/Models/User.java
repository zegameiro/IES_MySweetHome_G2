package mysweethome.MSHbackend.Models;

import org.springframework.data.annotation.Id;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document("users")
public class User {

    @Id
    private String uid;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;

    private String active_token;
    private String salt;
    
    List<String> dislikedPreferences = new LinkedList<>();

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getFirstName() {
        return firstname;
    }
    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }
    public String getLastName() {
        return lastname;
    }
    public void setLastName(String lastname) {
        this.lastname = lastname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List<String> getDislikedPreferences() {
        return dislikedPreferences;
    }
    public void setDislikedPreferences(List<String> dislikedPreferences) {
        this.dislikedPreferences = dislikedPreferences;
    }
    public void addDislike(String dislike) {
        this.dislikedPreferences.add(dislike);
    }
    public String getActive_Token() {
        return active_token;
    }
    public void setActive_Token(String active_Token) {
        this.active_token = active_Token;
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
}
