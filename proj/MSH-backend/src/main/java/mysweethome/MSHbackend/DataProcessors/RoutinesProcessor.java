package mysweethome.MSHbackend.DataProcessors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mysweethome.MSHbackend.Repositories.ActionRepository;
import mysweethome.MSHbackend.Repositories.RoutineRepository;
import mysweethome.MSHbackend.Services.ActionsService;
import mysweethome.MSHbackend.Services.RoutineService;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import mysweethome.MSHbackend.Models.Routine;
import mysweethome.MSHbackend.Models.Action;
import java.util.List;



@Component
public class RoutinesProcessor {

    @Autowired
    private RoutineService routines;

    @Autowired
    private ActionsService actions;


    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void processRoutine(){

        while(true){

        System.out.println("Checking for routines");

        List<Routine> routines = this.routines.findAllUntriggered();

        for ( Routine routine : routines ) {

            Action action = routine.getAssociated_action();



            System.out.println("Routine found: " + routine.toString());

        }

        try {
            Thread.sleep(10000); // check for routines every 10seconds
        } catch (InterruptedException e) {
            e.printStackTrace();

        }



    }
    
}
}
