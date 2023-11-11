package mysweethome.MSHbackend.Models;

import org.springframework.data.annotation.Id;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("actions")
public class Action {

    @Id
    private int id;
    @NotBlank
    private int action_category;
    @NotBlank
    private String action_description;
    @NotBlank
    private HashMap<String, String> input_ranges;
    @NotBlank
    private HashMap<String,Integer> output_actions;

}