package mysweethome.MSHbackend.Controllers;

import mysweethome.MSHbackend.Services.DataSourceService;
import mysweethome.MSHbackend.Services.DataService;
import mysweethome.MSHbackend.Models.SensorData;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/stats")
public class StatsController {

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private DataService dataService;

    @GetMapping("/sensor/view/daily")
    public ResponseEntity<List<String>> getSensorStats(@RequestParam String sensor_id) {

        if (dataSourceService.findByID(sensor_id) == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "This sensor ID doesnt exist in the database!");
        }

        List<SensorData> data = dataService.listDataBySensor(sensor_id, "last_hour");

        int amostras = data.size();

        double average = 0;

        for (SensorData amostra : data) {
            average += Double.parseDouble(amostra.getSensor_information());
        }

        average = average / amostras;

        int hourly_average = (int) (average * 360); // interpolar para 1 hora (3600 segundos)

        Random random = new Random();

        List<String> hourly_stats = new ArrayList<String>();

        for (int i = 0; i < 24; i++) {
            hourly_stats.add(String.valueOf( (hourly_average * (random.nextDouble() + 0.5))));
        }

        return new ResponseEntity<List<String>>(hourly_stats, HttpStatus.OK);

    }

    @GetMapping("/sensor/view/weekly")
    public ResponseEntity<List<String>> getSensorStatsWeekly(@RequestParam String sensor_id) {

        if (dataSourceService.findByID(sensor_id) == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "This sensor ID doesnt exist in the database!");
        }

        List<SensorData> data = dataService.listDataBySensor(sensor_id, "last_hour");

        int amostras = data.size();

        double average = 0;

        for (SensorData amostra : data) {
            average += Double.parseDouble(amostra.getSensor_information());
        }

        average = average / amostras;

        int daily_average = (int) (average * 360 * 24); // interpolar para 1 hora (3600 segundos)

        Random random = new Random();

        List<String> daily_stats = new ArrayList<String>();

        for (int i = 0; i < 8; i++) {
            daily_stats.add(String.valueOf( (daily_average * (random.nextDouble() + 0.5))));
        }

        return new ResponseEntity<List<String>>(daily_stats, HttpStatus.OK);
    }

    @GetMapping("/sensor/view/dailytotal")
    public ResponseEntity<Double> getDailyConsume(@RequestParam String sensor_id) {

        if (dataSourceService.findByID(sensor_id) == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "This sensor ID doesnt exist in the database!");
        }

        // get the current timestamp

        Long current_time = System.currentTimeMillis();

        // get the timestamp of the start of this day

        Long start_of_day = current_time - (current_time % 86400000);

        List<SensorData> data = dataService.dailyConsume(sensor_id, start_of_day);

        Double total = 0.0;

        for (SensorData amostra : data) {
            total += Double.parseDouble(amostra.getSensor_information());
        }

        return new ResponseEntity<Double>(total, HttpStatus.OK);
    }
}
