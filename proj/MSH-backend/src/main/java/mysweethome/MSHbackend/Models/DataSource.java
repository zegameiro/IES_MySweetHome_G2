package mysweethome.MSHbackend.Models;

import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotBlank;

@ToString
@Document("sensors")
public class DataSource  {

    @Id
    private String ID;
    @NotBlank
    private int device_category;
    private String device_location;
    @NotBlank
    private String name;

    public DataSource(String device_id, int device_category,String device_location, String name) {
        this.ID = device_id;
        this.device_category = device_category;
        this.device_location = device_location;
        this.name = name;
    }

    public DataSource() {
    }

    public String getDevice_id() {
        return ID;
    }

    public void setDevice_id(String device_id) {
        this.ID = device_id;
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

        public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
