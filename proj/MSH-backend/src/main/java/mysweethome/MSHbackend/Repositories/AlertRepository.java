package mysweethome.MSHbackend.Repositories;
import mysweethome.MSHbackend.Models.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.LinkedList;


public interface AlertRepository extends MongoRepository<Alert, String> {
    
        // adicionar + cenas conforme for preciso
        @Query("{}")
        public LinkedList<Alert> getAll();

        @Query("{'id': ?0}")
        public Alert findByAlertId(String id);
}
