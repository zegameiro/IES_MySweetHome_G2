package mysweethome.MSHbackend.Repositories;
import mysweethome.MSHbackend.Models.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import javax.persistence.OrderBy;

import org.springframework.data.mongodb.repository.Query;



@Repository
public interface DataRepository extends MongoRepository<SensorData, String> {

    @OrderBy("timestamp DESC")
    public List<SensorData> findBySourceid(String sensor_id);

    @OrderBy("timestamp DESC")
    public List<SensorData> findAll();

    
    @Query("{'sourceid': ?0, 'timestamp': {$gte: ?1}}")
    @OrderBy("timestamp DESC")
    public List<SensorData> findByIDTimeStamped(String sensor_id, long timestamp);


    
}
