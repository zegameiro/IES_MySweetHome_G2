package mysweethome.MSHbackend.Repositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import mysweethome.MSHbackend.Models.Routine;
import org.springframework.data.mongodb.repository.Query;

public interface RoutineRepository extends MongoRepository<Routine, Integer> {
    

        public Routine findById(int id);

        @Query ("{'triggered': false}")
        public java.util.List<Routine> findAllUntriggered();

    
}