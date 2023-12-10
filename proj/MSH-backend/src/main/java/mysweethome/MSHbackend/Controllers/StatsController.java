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
import mysweethome.MSHbackend.Models.DataSource;
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

        double hourly_multiplier = 360;

        Random random = new Random();

        List<String> hourly_stats = new ArrayList<String>();

        DataSource data_source = dataSourceService.findByID(sensor_id);

        if (data_source.getDevice_category() == 1) { // temperatura

            for (int i = 0; i < 24; i++) {
            hourly_stats.add(String.valueOf((average * (0.2 * random.nextDouble() + 0.9))));
            }
        }
        else if (data_source.getDevice_category() == 2) { // eletricidade

            for (int i = 0; i < 24; i++) {
            hourly_stats.add(String.valueOf((average * hourly_multiplier * (0.2 * random.nextDouble() + 0.9))));
            }

        } else { // implementar para o resto dos sensores, este ta a dar a media

            for (int i = 0; i < 24; i++) {
            hourly_stats.add(String.valueOf((average * (0.2 * random.nextDouble() + 0.9))));
            }

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

        DataSource data_source = dataSourceService.findByID(sensor_id);

        int daily_multiplier = 360 * 24;

        List<String> daily_stats = new ArrayList<String>();
        Random random = new Random();

        if (data_source.getDevice_category() == 1) { // temperatura
            for (int i = 0; i < 7; i++) {
                daily_stats.add(String.valueOf((average * (0.2 * random.nextDouble() + 0.9))));
            }
        } else if (data_source.getDevice_category() == 2) { // eletricidade
            for (int i = 0; i < 7; i++) {
                daily_stats.add(String.valueOf((average * daily_multiplier * (0.2 * random.nextDouble() + 0.9))));
            }

        } else { // implementar para o resto dos sensores, este ta a dar a media

            for (int i = 0; i < 7; i++) {
                daily_stats.add(String.valueOf((average * (0.2 * random.nextDouble() + 0.9))));
            }

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

        Double return_value = 0.0;
        for (SensorData amostra : data) {
            return_value += Double.parseDouble(amostra.getSensor_information());
        }

        DataSource sensor = dataSourceService.findByID(sensor_id);

        if (sensor.getDevice_category() == 1) { // temperatura , média
            double average = return_value / data.size();
            return new ResponseEntity<Double>(average, HttpStatus.OK);
        } else if (sensor.getDevice_category() == 2) // eletricidade , consumo total diário
        {
            return new ResponseEntity<Double>(return_value, HttpStatus.OK);
        } else { // ainda nao ta implementado, para os outros
            return new ResponseEntity<Double>(return_value, HttpStatus.OK);
        }

    }
}
