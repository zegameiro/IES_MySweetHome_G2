package mysweethome.MSHbackend.Models;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotBlank;

@ToString
@NoArgsConstructor
@Document("sensordata")
public class SensorData {

    @Id
    @GeneratedValue
    private String data_id;
    @NotBlank
    private String sourceid;
    @NotBlank
    private Long timestamp;
    @NotBlank
    private String sensor_information;

    public SensorData(String data_source_id, @NotBlank Long timestamp, @NotBlank String sensor_information) {
        this.sourceid = data_source_id;
        this.timestamp = timestamp;
        this.sensor_information = sensor_information;
    }

    public String getData_source_id() {
        return sourceid;
    }
    public void setData_source_id(String data_source_id) {
        this.sourceid = data_source_id;
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