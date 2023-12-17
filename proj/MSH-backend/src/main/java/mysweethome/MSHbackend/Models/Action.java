package mysweethome.MSHbackend.Models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("actions")
public class Action {

    @Id
    private String id;
    @CreatedDate
    private long timestamp;
    @NotBlank
    private String action_title;
    @NotBlank
    private String action_description;
    @NotBlank
    private String outputDeviceID; // id of the output device that will be affected by this action
    @NotBlank
    private boolean done = false;

    public void setId(){
        this.id = UUID.randomUUID().toString();
    }


    public void execute() {

        System.out.println("Executing action: " + this.action_title);

    }

}