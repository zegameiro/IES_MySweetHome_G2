package mysweethome.MSHbackend.Models;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotBlank;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("outputdevices")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OutputDevice  {

    @Id
    private String ID;
    @NotBlank
    private String name;
    @NotBlank
    private OutputDeviceType device_category;
    @NotBlank
    private String device_location; // may be blank
    @NotBlank
    private String current_state;
    @NotBlank
    private Long laststatechange;

    Integer temperature; // for air conditioners
    String current_channel; // for television
    String current_music; // for speakers
    String color; // for lights




}
