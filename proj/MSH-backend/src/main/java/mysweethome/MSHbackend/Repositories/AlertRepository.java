package mysweethome.MSHbackend.Repositories;
import mysweethome.MSHbackend.Models.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.LinkedList;


public interface AlertRepository extends MongoRepository<Alert, String> {
    
        @Query("{'marked_as_read': false}")
        public LinkedList<Alert> getAllUnread();

        @Query("{}")
        public LinkedList<Alert> getAll();

        @Query("{'id': ?0 , 'marked_as_read': false}")
        public Alert findByAlertId(String id);
}
