package mysweethome.MSHbackend.Models;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotBlank;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("alerts")
public class Alert {

    @Id
    private int data_source_id;
    @NotBlank
    private int timestamp;
    @NotBlank
    private String alert_information;

}