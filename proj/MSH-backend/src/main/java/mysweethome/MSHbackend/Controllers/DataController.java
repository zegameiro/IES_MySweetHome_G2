package mysweethome.MSHbackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;
import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@RestController
@RequestMapping(path = "/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping("/view") // /view?sensor_id=12345
    public ResponseEntity<List<SensorData>> listDataBySensor(@RequestParam("sensor_id") int sensor_id) {
        List<SensorData> data = dataService.listDataBySensor(sensor_id);
        // .subList(0, 5);
        return ResponseEntity.ok(data);

    }

    @GetMapping("/list") // para todos
    public ResponseEntity<List<SensorData>> listAllData ()
    {
        try{

        List<SensorData> data = dataService.listAllData();
        return ResponseEntity.ok(data);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal processing error!");
        }
    }



}
