package mysweethome.MSHbackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mysweethome.MSHbackend.Repositories.DataRepository;
import mysweethome.MSHbackend.Models.SensorData;
import java.util.List;
import java.util.ArrayList;


@Service
public class DataService {

    @Autowired
    private DataRepository dataRepository;

    public void saveData(SensorData data) {
        dataRepository.save(data);
    }

    /* 
    public List<SensorData> listData() {
        return dataSourceRepository.findAllByUser();
    }
    */
    public List<SensorData> listDataBySensor(String sensor_id, String filter) {

        List<SensorData> data = new ArrayList<SensorData>();

        if (filter.equals("none")){
        return dataRepository.findByDatasourceid(sensor_id);
        }
        else if (filter.equals("last_hour")){
            Long current_time = System.currentTimeMillis();
            return dataRepository.findByIDTimeStamped(sensor_id,current_time - 3600000);
        }
        else if (filter.equals("last_week")){
            Long current_time = System.currentTimeMillis();
            return dataRepository.findByIDTimeStamped(sensor_id,current_time - 604800000);
        }
        else if (filter.equals("last_month")){
            Long current_time = System.currentTimeMillis();
            return dataRepository.findByIDTimeStamped(sensor_id,current_time - 2592000000L);
        }
        else if (filter.equals("last_day")){
            Long current_time = System.currentTimeMillis();
            return dataRepository.findByIDTimeStamped(sensor_id,current_time - 86400000);
    
        }

        else if (filter.equals("latest")){
            data.add(dataRepository.findByDatasourceid(sensor_id).get(dataRepository.findByDatasourceid(sensor_id).size()-1));
            return data;
        }

        return null;
    }

    public List<SensorData> listAllData() {
        return dataRepository.findAll();
    }

    
}