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
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import mysweethome.MSHbackend.Models.DataSource;
import mysweethome.MSHbackend.Models.SensorStats;
import java.util.Collections;
import java.util.Comparator;

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

        List<SensorData> data = dataService.listDataBySensor(sensor_id, "last_day");

        ArrayList<String> hourly_stats = new ArrayList<String>();

        double hourSUM = 0;
        int hourCOUNT = 0;
        int numHours = 0;

        //  Create a calendar with the date set to the beginning of the previous hour
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long nextHour = cal.getTimeInMillis() - 3600000;
        long today = cal.getTimeInMillis();

        Collections.sort(data, Comparator.comparing(SensorData::getTimestamp));
        Collections.reverse(data);

        for (SensorData amostra : data) {
            if (amostra.getTimestamp() > today) {
                continue;
            }
            while (amostra.getTimestamp() < nextHour - ( 3600000 * numHours) && numHours < 24) {
                if (hourCOUNT == 0) {
                    hourly_stats.add("0");
                }
                else {
                    hourly_stats.add(String.valueOf(hourSUM / hourCOUNT));
                }
                numHours++;          
                hourSUM = 0;
                hourCOUNT = 0; 
            }
            if (numHours >= 24) {
                break;
            }   
            hourSUM += Double.parseDouble(amostra.getSensor_information());
            hourCOUNT++;
        }

        //  Ran out of data before day finished
        if (hourCOUNT != 0) {
            hourly_stats.add(String.valueOf(hourSUM / hourCOUNT));
            numHours++;
        }

        while (numHours < 24) {
            hourly_stats.add("0");
            numHours++;
        }

        System.out.println(hourly_stats);
        
        Collections.reverse(hourly_stats);

        DataSource data_source = dataSourceService.findByID(sensor_id);

        SensorStats sensor_stats = new SensorStats(data_source.getDevice_category(), data.get(0).getUnit(), hourly_stats);

        return new ResponseEntity<SensorStats>(sensor_stats, HttpStatus.OK);

    }

    @GetMapping("/sensor/view/weekly")
    public ResponseEntity<SensorStats> getSensorStatsWeekly(@RequestParam String sensor_id) {

        if (dataSourceService.findByID(sensor_id) == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "This sensor ID doesnt exist in the database!");
        }

        List<SensorData> data = dataService.listDataBySensor(sensor_id, "last_week");

        ArrayList<String> daily_stats = new ArrayList<String>();

        double daySUM = 0;
        int dayCOUNT = 0;
        int numDays = 0;

        //  Create a calendar with the date set to the beginning of the previous hour
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long nextDay = cal.getTimeInMillis() - 86400000;
        long today = cal.getTimeInMillis();

        Collections.sort(data, Comparator.comparing(SensorData::getTimestamp));
        Collections.reverse(data);

        for (SensorData amostra : data) {
            if (amostra.getTimestamp() > today) {
                continue;
            }
            while (amostra.getTimestamp() < nextDay - ( 86400000 * numDays) && numDays < 7) {
                if (dayCOUNT == 0) {
                    daily_stats.add("0");
                }
                else {
                    daily_stats.add(String.valueOf(daySUM / dayCOUNT));
                }
                numDays++;          
                daySUM = 0;
                dayCOUNT = 0; 
            }
            if (numDays >= 7) {
                break;
            }   
            daySUM += Double.parseDouble(amostra.getSensor_information());
            dayCOUNT++;
        }

        //  Ran out of data before day finished
        if (dayCOUNT != 0) {
            daily_stats.add(String.valueOf(daySUM / dayCOUNT));
            numDays++;
        }

        while (numDays < 7) {
            daily_stats.add("0");
            numDays++;
        }

        System.out.println(daily_stats);
        
        Collections.reverse(daily_stats);

        DataSource data_source = dataSourceService.findByID(sensor_id);

        SensorStats sensor_stats = new SensorStats(data_source.getDevice_category(),data.get(0).getUnit(), daily_stats);

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
