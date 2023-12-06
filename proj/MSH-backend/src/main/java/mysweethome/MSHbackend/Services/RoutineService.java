package mysweethome.MSHbackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mysweethome.MSHbackend.Repositories.RoutineRepository;
import mysweethome.MSHbackend.Models.Routine;
import java.util.List;

@Service
public class RoutineService {

    @Autowired
    private RoutineRepository routineRepository;

    public void saveRoutine(Routine routine) {
        routineRepository.save(routine);
    }

    public Routine saveGetRoutine(Routine routine) {
        return routineRepository.save(routine);
    }

    public List<Routine> findAll() {
        return routineRepository.findAll();
    }

    public List<Routine> findAllUntriggered() {
        return routineRepository.findAllUntriggered();
    }

    public Routine findById(int id) {
        return routineRepository.findById(id);
    }

}