package mysweethome.MSHbackend.Repositories;
import mysweethome.MSHbackend.Models.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import mysweethome.MSHbackend.Models.User;


@Repository
public interface DataRepository extends MongoRepository<SensorData, String> {

    // adicionar + cenas conforme for preciso
    
}
