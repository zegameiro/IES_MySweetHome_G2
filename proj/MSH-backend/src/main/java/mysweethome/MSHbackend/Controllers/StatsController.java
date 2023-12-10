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
import mysweethome.MSHbackend.Models.SensorStats;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/stats")
public class StatsController {

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private DataService dataService;

    @GetMapping("/sensor/view/daily")
    public ResponseEntity<SensorStats> getSensorStats(@RequestParam String sensor_id) {

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

        ArrayList<String> hourly_stats = new ArrayList<String>();

        DataSource data_source = dataSourceService.findByID(sensor_id);

        if (data_source.getDevice_category() == 1) { // temperatura

            for (int i = 0; i < 24; i++) {
                hourly_stats.add(String.valueOf((average * (0.2 * random.nextDouble() + 0.9))));
            }
        } else if (data_source.getDevice_category() == 2) { // eletricidade

            for (int i = 0; i < 24; i++) {
                hourly_stats.add(String.valueOf((average * hourly_multiplier * (0.2 * random.nextDouble() + 0.9))));
            }

        } else { // implementar para o resto dos sensores, este ta a dar a media

            for (int i = 0; i < 24; i++) {
                hourly_stats.add(String.valueOf((average * (0.2 * random.nextDouble() + 0.9))));
            }

        }

        SensorStats sensor_stats = new SensorStats(data_source.getDevice_category(),data.get(0).getUnit(),hourly_stats);

        return new ResponseEntity<SensorStats>(sensor_stats, HttpStatus.OK);

    }

    @GetMapping("/sensor/view/weekly")
    public ResponseEntity<SensorStats> getSensorStatsWeekly(@RequestParam String sensor_id) {

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

        ArrayList<String> daily_stats = new ArrayList<String>();
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

        SensorStats sensor_stats = new SensorStats(data_source.getDevice_category(),data.get(0).getUnit(),daily_stats);

        return new ResponseEntity<SensorStats>(sensor_stats, HttpStatus.OK);
    }

    @GetMapping("/sensor/view/dailytotal")
    public ResponseEntity<SensorStats> getDailyConsume(@RequestParam String sensor_id) {

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

        SensorStats sensor_stats = new SensorStats();
        sensor_stats.setCategory(sensor.getDevice_category());
        sensor_stats.setUnit(data.get(0).getUnit());
        sensor_stats.setValues(new ArrayList<String>());

        if (sensor.getDevice_category() == 1) { // temperatura , média
            double average = return_value / data.size();
            sensor_stats.getValues().add(String.valueOf(average));
            return new ResponseEntity<SensorStats>( sensor_stats, HttpStatus.OK);
        } else if (sensor.getDevice_category() == 2) // eletricidade , consumo total diário
        {
            sensor_stats.getValues().add(String.valueOf(return_value));
            return new ResponseEntity<SensorStats>(sensor_stats, HttpStatus.OK);
        } else { // ainda nao ta implementado, para os outros
            sensor_stats.getValues().add(String.valueOf(return_value));
            return new ResponseEntity<SensorStats>(sensor_stats, HttpStatus.OK);
        }

    }

}
