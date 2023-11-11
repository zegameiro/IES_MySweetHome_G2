package mysweethome.MSHbackend.Models;

import org.springframework.data.annotation.Id;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

}
