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
@Document("sensors")
public class DataSource {

    @Id
    private int device_id;
    @NotBlank
    private int device_category;
    @NotBlank
    private String device_location;

}
