package mysweethome.MSHbackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import mysweethome.MSHbackend.Models.TimeBasedRoutine;
import mysweethome.MSHbackend.Models.SensorBasedRoutine;
import mysweethome.MSHbackend.Repositories.SBRepo;
import mysweethome.MSHbackend.Repositories.TBRepo;
import java.util.List;

@Service
public class RoutineService {

    @Autowired
    private TBRepo timeroutineRepository;

    @Autowired
    private SBRepo sensorRoutineRepository;

    public void saveTBRoutine(TimeBasedRoutine routine) {
        timeroutineRepository.save(routine);
    }

    public void saveSBRoutine(SensorBasedRoutine routine) {
        sensorRoutineRepository.save(routine);
    }

    public List<TimeBasedRoutine> findAllTB() {
        return timeroutineRepository.findAll();
    }

    public List<SensorBasedRoutine> findAllSB() {
        return sensorRoutineRepository.findAll();
    }

    public List<TimeBasedRoutine> findAllTBUntriggered() {
        return timeroutineRepository.findAllUntriggered();
    }

    public List<SensorBasedRoutine> findAllSBUntriggered() {
        return sensorRoutineRepository.findAllUntriggered();
    }

    public List<SensorBasedRoutine> findallSB() {
        return sensorRoutineRepository.findAll();
    }

    public List<TimeBasedRoutine> findallTB() {
        return timeroutineRepository.findAll();
    }


}