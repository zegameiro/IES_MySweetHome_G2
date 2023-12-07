package mysweethome.MSHbackend.Models;

import org.springframework.data.annotation.Id;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotBlank;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document("rooms")
public class Room {

    @Id
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private Integer floornumber;

    List<String> devices = new LinkedList<String>();

    public String getId() {
        return id;
    }
    public void setId(String uid) {
        this.id = uid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getFloornumber() {
        return floornumber;
    }
    public void setFloornumber(Integer floornumber) {
        this.floornumber = floornumber;
    }

    public List<String> getDevices() {
        return devices;
    }
    public void setDevices(List<String> devices) {
        this.devices = devices;
    }
    public void addDevice(String dev) {
        this.devices.add(dev);
    }
    
}
