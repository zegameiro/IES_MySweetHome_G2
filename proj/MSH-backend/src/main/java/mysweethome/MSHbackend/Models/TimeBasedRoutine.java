package mysweethome.MSHbackend.Models;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("time_based_routines")
public class TimeBasedRoutine {

    @NotBlank
    private Long trigger_timestamp; // timestamp when this routine should be activated
    @NotBlank
    private Action associated_action;
    @NotBlank
    private boolean triggered = false;
    @NotBlank
    private String routine_name;

    @Id
    private String id = generateDefaultId(); // Default value for id field

    private String generateDefaultId() {
        return UUID.randomUUID().toString();
    }

    public void setId() {
        this.id = generateDefaultId();
    }
}
