package mysweethome.MSHbackend.Repositories;
import mysweethome.MSHbackend.Models.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface DataRepository extends MongoRepository<SensorData, String> {

    // adicionar + cenas conforme for preciso

    

    public List<SensorData> findByDatasourceid(int sensor_id);

    public List<SensorData> findAll();
    
}
