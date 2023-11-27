package mysweethome.MSHbackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping("/view")
    public ResponseEntity<List<SensorData>> listDataBySensor(
            @RequestParam("sensor_id") int sensor_id,
            @RequestParam(name = "filter", defaultValue = "none", required = false) String filter) {
        List<SensorData> data = new ArrayList<SensorData>();

        if (!filter.equals("last_hour") && !filter.equals("last_day") && !filter.equals("last_week")
                && !filter.equals("last_month") && !filter.equals("none") && !filter.equals("latest"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filter!");
            
            data = dataService.listDataBySensor(sensor_id, filter);

        if (data == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error retrieving data or there is no data!");

        if (data.size() == 0)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no data!");

        return ResponseEntity.ok(data);
    }

    @GetMapping("/list") // get all data from all sensors
    public ResponseEntity<List<SensorData>> listAllData() {
        try {

            List<SensorData> data = dataService.listAllData();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }
    }

}

