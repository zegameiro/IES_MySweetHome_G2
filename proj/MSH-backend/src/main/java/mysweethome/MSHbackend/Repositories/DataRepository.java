package mysweethome.MSHbackend.Repositories;
import mysweethome.MSHbackend.Models.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DataRepository extends MongoRepository<SensorData, String> {

    // adicionar + cenas conforme for preciso
    
}
