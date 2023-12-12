package mysweethome.MSHbackend.Models;

import org.springframework.data.annotation.Id;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("routines")
public class Routine {

    @Id
    private int id;
    @NotBlank
    private List<String> trigger_values;
    @NotBlank
    private Action associated_action;
    @NotBlank
    private boolean triggered = false;
}
