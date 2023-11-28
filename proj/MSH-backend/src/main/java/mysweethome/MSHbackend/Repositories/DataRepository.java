package mysweethome.MSHbackend.Repositories;
import mysweethome.MSHbackend.Models.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;



@Repository
public interface DataRepository extends MongoRepository<SensorData, String> {

    public List<SensorData> findBySourceid(String sensor_id);

    public List<SensorData> findAll();

    @Query("{'sourceid': ?0, 'timestamp': {$gte: ?1}}")
    public List<SensorData> findByIDTimeStamped(String sensor_id, long timestamp);


    
}
