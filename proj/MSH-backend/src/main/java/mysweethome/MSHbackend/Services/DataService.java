package mysweethome.MSHbackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mysweethome.MSHbackend.Repositories.DataRepository;
import mysweethome.MSHbackend.Models.SensorData;


@Service
public class DataService {

    @Autowired
    private DataRepository dataSourceRepository;

    public void saveData(SensorData data) {
        dataSourceRepository.save(data);
    }

    
}