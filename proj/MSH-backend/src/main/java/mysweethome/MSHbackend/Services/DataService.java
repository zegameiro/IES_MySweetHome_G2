package mysweethome.MSHbackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mysweethome.MSHbackend.Repositories.DataRepository;
import mysweethome.MSHbackend.Models.SensorData;
import mysweethome.MSHbackend.Models.User;
import org.springframework.data.mongodb.core.query.Query;
import org.json.JSONObject;
import java.util.List;


@Service
public class DataService {

    @Autowired
    private DataRepository dataRepository;

    public void saveData(SensorData data) {
        dataRepository.save(data);
    }

    
}