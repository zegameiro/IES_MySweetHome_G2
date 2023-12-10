package mysweethome.MSHbackend.DataProcessors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mysweethome.MSHbackend.Repositories.ActionRepository;
import mysweethome.MSHbackend.Services.AlertService;
import mysweethome.MSHbackend.Services.RoutineService;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import mysweethome.MSHbackend.Models.Routine;
import mysweethome.MSHbackend.Models.SensorData;
import mysweethome.MSHbackend.Models.Action;
import mysweethome.MSHbackend.Services.DataService;
import mysweethome.MSHbackend.Models.Alert;
import java.util.List;

@Component
public class RoutinesProcessor {

    @Autowired
    private RoutineService routineService;

    @Autowired
    private DataService dataService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ActionRepository actions;

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void processRoutine() {

        while (true) {

            System.out.println("Checking for routines");

            List<Routine> routines = routineService.findAllUntriggered();

            for (Routine routine : routines) {

                Action action = routine.getAssociated_action();

                List<String> trigger_values = routine.getTrigger_values();

                List<SensorData> sensorData = dataService.listDataBySensor(action.getOutputDeviceID(), "latest");

                SensorData latest = sensorData.get(0);

                for (String trigger_value : trigger_values) {

                    if (latest.getSensor_information().equals(trigger_value)) {

                        routine.setTriggered(true);

                        System.out.println("Routine triggered: " + routine.toString());

                        Alert alert = new Alert();
                        alert.setAlert_header("Routine " + routine.getId() + " triggered");
                        alert.setAlert_information("The routine " + routine.getId() + " was triggered.");
                        alert.setAlert_level(2);

                        action.setDone(true);

                        actions.save(action);

                        alertService.saveAlert(alert);

                        routineService.saveRoutine(routine);

                        action.execute();

                        break;

                    }

                }

                System.out.println("Routine found: " + routine.toString());

            }

            try {
                Thread.sleep(5000); // check for routines every 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();

            }

        }

    }
}
