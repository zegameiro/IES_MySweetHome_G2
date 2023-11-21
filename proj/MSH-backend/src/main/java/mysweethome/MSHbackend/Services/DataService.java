package mysweethome.MSHbackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mysweethome.MSHbackend.Repositories.DataRepository;
import mysweethome.MSHbackend.Models.SensorData;
import java.util.List;


@Service
public class DataService {

    @Autowired
    private DataRepository dataSourceRepository;

    public void saveData(SensorData data) {
        dataSourceRepository.save(data);
    }

    /* 
    public List<SensorData> listData() {
        return dataSourceRepository.findAllByUser();
    }
    */
    public List<SensorData> listDataBySensor(int sensor_id) {
        return dataSourceRepository.findByDatasourceid(sensor_id);
    }

    public List<SensorData> listAllData() {
        return dataSourceRepository.findAll();
    }

    
}