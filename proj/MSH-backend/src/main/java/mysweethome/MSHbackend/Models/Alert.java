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
    private String id;
    @NotBlank
    private int timestamp;
    //  1: Immediate action, 2: Information (ex: action executed), 3: New device added
    @NotBlank
    private int alert_level;
    @NotBlank
    private String alert_header;
    @NotBlank
    private String alert_information;
    @NotBlank
    private boolean marked_as_read = false;


}