package mysweethome.MSHbackend.Models;

import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotBlank;

@ToString
@Document("sensors")
public class DataSource {

    @Id
    private int device_id;
    @NotBlank
    private int device_category;
    @NotBlank
    private String device_location;

    public DataSource(int device_id, int device_category, String device_location) {
        this.device_id = device_id;
        this.device_category = device_category;
        this.device_location = device_location;
    }

    public DataSource() {
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getDevice_category() {
        return device_category;
    }

    public void setDevice_category(int device_category) {
        this.device_category = device_category;
    }

    public String getDevice_location() {
        return device_location;
    }

    public void setDevice_location(String device_location) {
        this.device_location = device_location;
    }

}
