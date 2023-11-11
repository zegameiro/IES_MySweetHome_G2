package mysweethome.MSHbackend.Repositories;
import mysweethome.MSHbackend.Models.Action;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActionRepository extends MongoRepository<Action, String> {
    
        // adicionar + cenas conforme for preciso
    
}
