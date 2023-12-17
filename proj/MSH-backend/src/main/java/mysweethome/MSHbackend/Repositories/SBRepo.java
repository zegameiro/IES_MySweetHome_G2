package mysweethome.MSHbackend.Repositories;
import org.springframework.data.mongodb.repository.MongoRepository;

import mysweethome.MSHbackend.Models.SensorBasedRoutine;
import org.springframework.data.mongodb.repository.Query;

public interface SBRepo extends MongoRepository<SensorBasedRoutine, Integer> {
    

        public SensorBasedRoutine findById(int id);

        @Query ("{'triggered': false}")
        public java.util.List<SensorBasedRoutine> findAllUntriggered();

    
}