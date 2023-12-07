package mysweethome.MSHbackend.Models;

import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotBlank;

@ToString
@Document("sensors")
public class DataSource  {

    @Id
    private String id;
    @NotBlank
    private int category;
    private String location;
    @NotBlank
    private String name;

    public DataSource(String id, int category,String location, String name) {
        this.id = id;
        this.category = category;
        this.location = location;
        this.name = name;
    }

    public DataSource() {
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

        public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
