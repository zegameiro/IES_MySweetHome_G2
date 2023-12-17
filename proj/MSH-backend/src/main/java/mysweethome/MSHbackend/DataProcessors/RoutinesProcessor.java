package mysweethome.MSHbackend.DataProcessors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mysweethome.MSHbackend.Repositories.ActionRepository;
import mysweethome.MSHbackend.Services.AlertService;
import mysweethome.MSHbackend.Services.RoutineService;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import mysweethome.MSHbackend.Models.TimeBasedRoutine;
import mysweethome.MSHbackend.Models.SensorBasedRoutine;
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

            checkforSBRoutines();
            checkforTBRoutines();

            try {
                Thread.sleep(5000); // check for routines every 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();

            }

        }

    }

    public void checkforTBRoutines() {

        List<TimeBasedRoutine> routines = routineService.findAllTBUntriggered();

        for (TimeBasedRoutine routine : routines) {

            Action action = routine.getAssociated_action();

            Long trigger_timestamp = routine.getTrigger_timestamp();

            if (System.currentTimeMillis() >= trigger_timestamp) {

                routine.setTriggered(true);

                System.out.println("Routine triggered: " + routine.toString());

                Alert alert = new Alert();
                alert.setAlert_header("Routine " + routine.getId() + " was activated");
                alert.setAlert_information("The routine " + routine.getId() + " was triggered.");
                alert.setAlert_level(2);

                action.setDone(true);

                actions.save(action);

                alertService.saveAlert(alert);

                routineService.saveTBRoutine(routine);

                action.execute();

                break;

            }
        }

    }

    public void checkforSBRoutines() {

        List<SensorBasedRoutine> routines = routineService.findAllSBUntriggered();

        for (SensorBasedRoutine routine : routines) {

            if (routine.getTrigger_type().equals("range"))
            {

                int start_value = Integer.parseInt(routine.getInput_ranges().get(0));
                int end_value = Integer.parseInt(routine.getInput_ranges().get(1));

                List<SensorData> data = dataService.listDataBySensor(routine.getSource_id(),"latest");

                SensorData latest_data = data.get(0);

                int value; 

                try { 

                value = Integer.parseInt(latest_data.getSensor_information());

                } catch (Exception e) {
                    continue; // ignorar alertas sensor based com valores que nao sao int por enquanto
                }

                if (value >= start_value && value <= end_value) {

                    routine.setTriggered(true);

                    System.out.println("Routine triggered: " + routine.toString());

                    Alert alert = new Alert();
                    alert.setAlert_header("Routine " + routine.getRoutine_name() + " was activated");
                    alert.setAlert_information("The routine " + routine.getRoutine_name() + " was triggered.");
                    alert.setAlert_level(2);

                    alertService.saveAlert(alert);

                    routineService.saveSBRoutine(routine);

                    routine.getAssociated_action().execute();

                    break;

                }
            }

            else if (routine.getTrigger_type().equals("exact")){

                List<SensorData> data = dataService.listDataBySensor(routine.getSource_id(),"latest");

                int trigger_value = Integer.parseInt(routine.getExact_value());

                SensorData latest_data = data.get(0);
                
                if (Integer.parseInt(latest_data.getSensor_information()) == trigger_value) {

                    routine.setTriggered(true);

                    System.out.println("Routine triggered: " + routine.toString());

                    Alert alert = new Alert();
                    alert.setAlert_header("Routine " + routine.getRoutine_name() + " was activated");
                    alert.setAlert_information("The routine " + routine.getRoutine_name() + " was triggered.");
                    alert.setAlert_level(2);

                    alertService.saveAlert(alert);

                    routineService.saveSBRoutine(routine);

                    routine.getAssociated_action().execute();

                    break;

                } 

            }

            else {
                System.out.println("Routine " + routine.getId() + " has invalid trigger type!");
            }



        }

    }

}
