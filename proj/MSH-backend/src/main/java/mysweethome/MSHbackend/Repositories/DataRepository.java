package mysweethome.MSHbackend.Repositories;
import mysweethome.MSHbackend.Models.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataRepository extends MongoRepository<SensorData, String> {

    // adicionar + cenas conforme for preciso
    
}
