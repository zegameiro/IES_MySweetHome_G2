package mysweethome.MSHbackend.Models;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotBlank;

@ToString
@NoArgsConstructor
@Document("sensordata")
public class SensorData {

    @Id
    private int data_source_id;
    @NotBlank
    private Long timestamp;
    @NotBlank
    private String sensor_information;

    public SensorData(int data_source_id, @NotBlank Long timestamp, @NotBlank String sensor_information) {
        this.data_source_id = data_source_id;
        this.timestamp = timestamp;
        this.sensor_information = sensor_information;
    }

    public int getData_source_id() {
        return data_source_id;
    }
    public void setData_source_id(int data_source_id) {
        this.data_source_id = data_source_id;
    }
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    public String getSensor_information() {
        return sensor_information;
    }
    public void setSensor_information(String sensor_information) {
        this.sensor_information = sensor_information;
    }
}