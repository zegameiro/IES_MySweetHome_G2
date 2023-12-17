package mysweethome.MSHbackend.Repositories;
import org.springframework.data.mongodb.repository.MongoRepository;

import mysweethome.MSHbackend.Models.TimeBasedRoutine;
import org.springframework.data.mongodb.repository.Query;

public interface TBRepo extends MongoRepository<TimeBasedRoutine, Integer> {
    

        public TimeBasedRoutine findById(String id);

        @Query ("{'triggered': false}")
        public java.util.List<TimeBasedRoutine> findAllUntriggered();



    
}
