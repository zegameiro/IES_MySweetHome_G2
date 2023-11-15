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
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;

    List<String> dislikedPreferences = new LinkedList<>();

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    
}
