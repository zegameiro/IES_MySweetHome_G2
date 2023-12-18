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
import mysweethome.MSHbackend.Services.OutputDeviceService;
import mysweethome.MSHbackend.Models.Alert;
import mysweethome.MSHbackend.Models.OutputDevice;
import mysweethome.MSHbackend.Models.OutputDeviceType;

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

    @Autowired
    private OutputDeviceService outputDevService;

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

            if (routine.isActive() == false) {
                continue;
            }

            Action action = routine.getAssociated_action();

            Long trigger_timestamp = routine.getTrigger_timestamp();

            if (System.currentTimeMillis() >= trigger_timestamp) {

                routine.setTriggered(true);

                System.out.println("Routine triggered: " + routine.toString());

                Alert alert = new Alert();
                alert.setAlert_header("Routine " + routine.getRoutine_name() + " was activated");
                alert.setAlert_information("The routine " + routine.getRoutine_name() + " was triggered.");
                alert.setAlert_level(2);
                alert.setTimestamp((int) (System.currentTimeMillis() / 1000L));

                action.setDone(true);

                actions.save(action);

                alertService.saveAlert(alert);

                routineService.saveTBRoutine(routine);

                action.execute();
                executeAction(action);

                break;

            }
        }

    }

    public void checkforSBRoutines() {

        List<SensorBasedRoutine> routines = routineService.findAllSBUntriggered();

        for (SensorBasedRoutine routine : routines) {

            if (routine.isActive() == false) {
                continue;
            }

            if (routine.getTrigger_type().equals("range")) {

                Double start_value = Double.parseDouble(routine.getInput_ranges().get(0));
                Double end_value = Double.parseDouble(routine.getInput_ranges().get(1));

                List<SensorData> data = dataService.listDataBySensor(routine.getSource_id(), "latest");

                SensorData latest_data = data.get(0);

                Double value;

                try {

                    value = Double.parseDouble(latest_data.getSensor_information());

                } catch (Exception e) {
                    continue; // ignorar alertas sensor based com valores que nao sao int por enquanto
                }

                if (value >= start_value && value <= end_value) {

                    routine.setTriggered(true);
                    routine.setActive(false);

                    System.out.println("Routine triggered: " + routine.toString());

                    Alert alert = new Alert();
                    alert.setAlert_header("Routine " + routine.getRoutine_name() + " was activated");
                    alert.setAlert_information("The routine " + routine.getRoutine_name() + " was triggered.");
                    alert.setAlert_level(2);
                    alert.setTimestamp((int) (System.currentTimeMillis() / 1000L));

                    alertService.saveAlert(alert);

                    routineService.saveSBRoutine(routine);

                    routine.getAssociated_action().execute();
                    executeAction(routine.getAssociated_action());

                    break;

                }
            }

            else if (routine.getTrigger_type().equals("exact")) {

                List<SensorData> data = dataService.listDataBySensor(routine.getSource_id(), "latest");

                Double trigger_value = Double.parseDouble(routine.getExact_value());

                SensorData latest_data = data.get(0);

                if (Double.parseDouble(latest_data.getSensor_information()) == trigger_value) {

                    routine.setTriggered(true);
                    routine.setActive(false);

                    System.out.println("Routine triggered: " + routine.toString());

                    Alert alert = new Alert();
                    alert.setAlert_header("Routine " + routine.getRoutine_name() + " was activated");
                    alert.setAlert_information("The routine " + routine.getRoutine_name() + " was triggered.");
                    alert.setAlert_level(2);
                    alert.setTimestamp((int) (System.currentTimeMillis() / 1000L));

                    alertService.saveAlert(alert);

                    routineService.saveSBRoutine(routine);

                    routine.getAssociated_action().execute();
                    executeAction(routine.getAssociated_action());

                    break;

                }

            }

            else {
                System.out.println("Routine " + routine.getId() + " has invalid trigger type!");
            }

        }
    }

    public void executeAction(Action assocAction) {
        String outDevID = assocAction.getOutputDeviceID();
        OutputDevice outDev = outputDevService.findByID(outDevID);

        System.out.println("RAN ROUTINE > " + assocAction.getAction_description());

        //  Turning OFF the device
        if (assocAction.getAction_description().equals(outDev.getDevice_category().getPossibleActions().get(1))) {
            outDev.setCurrent_state("0");
        }
        //  Turning ON the device
        else if (assocAction.getAction_description().equals(outDev.getDevice_category().getPossibleActions().get(0))) {
            outDev.setCurrent_state("1");
        }

        //  Device is a television, so we can change channels
        if (outDev.getDevice_category() == OutputDeviceType.TELEVISION) {
            //  If the action was not an Turn ON or Turn Off, set the TV to a new channel (and make sure it is )
            if (assocAction.getAction_description().equals(outDev.getDevice_category().getPossibleActions().get(2))) {
                outDev.setCurrent_state("1");
                //  Update with the chosen channel
                outDev.setCurrent_channel(assocAction.getAction_newValue());
            }
            else if (assocAction.getAction_description().equals(outDev.getDevice_category().getPossibleActions().get(3))) {
                outDev.setCurrent_state("1");
                outDev.setSlider_value(assocAction.getAction_newValue());
            }
        }
        if (outDev.getDevice_category() == OutputDeviceType.DEHUMIDIFER) {
            if (assocAction.getAction_description().equals(outDev.getDevice_category().getPossibleActions().get(2))) {
                outDev.setCurrent_state("1");
                outDev.setTemperature(Integer.parseInt(assocAction.getAction_newValue()));
            }
        }
        if (outDev.getDevice_category() == OutputDeviceType.LIGHT) {
            if (assocAction.getAction_description().equals(outDev.getDevice_category().getPossibleActions().get(2))) {
                outDev.setCurrent_state("1");
                outDev.setColor(assocAction.getAction_newValue());
            }
            else if (assocAction.getAction_description().equals(outDev.getDevice_category().getPossibleActions().get(3))) {
                outDev.setCurrent_state("1");
                outDev.setSlider_value(assocAction.getAction_newValue());
            }
        }
        if (outDev.getDevice_category() == OutputDeviceType.AIR_CONDITIONER) {
            if (assocAction.getAction_description().equals(outDev.getDevice_category().getPossibleActions().get(2))) {
                outDev.setCurrent_state("1");
                outDev.setTemperature(Integer.parseInt(assocAction.getAction_newValue()));
            }
        }
        if (outDev.getDevice_category() == OutputDeviceType.SPEAKER) {
            if (assocAction.getAction_description().equals(outDev.getDevice_category().getPossibleActions().get(2))) {
                outDev.setCurrent_state("1");
                outDev.setCurrent_music(assocAction.getAction_newValue());
            }
            else if (assocAction.getAction_description().equals(outDev.getDevice_category().getPossibleActions().get(3))) {
                outDev.setCurrent_state("1");
                outDev.setSlider_value(assocAction.getAction_newValue());
            }
        }

        outputDevService.saveOutputDevice(outDev);

        return;
    }

}
