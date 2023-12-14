package mysweethome.MSHbackend.Models;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("sensor_based_routines")
public class SensorBasedRoutine {
    @NotBlank
    private String trigger_type; // "range" or "exact"
    private List<String> input_ranges;
    private String exact_value;
    /*
     * This routines may be triggered by a sensor value thats within a certain range
     * or by an exact value
     */
    @NotBlank
    private Action associated_action;
    @NotBlank
    private boolean triggered = false;
    @NotBlank
    private String source_id;

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
