package mysweethome.MSHbackend.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mysweethome.MSHbackend.Models.Action;

import mysweethome.MSHbackend.Repositories.ActionRepository;

@Service
public class ActionsService {

    @Autowired
    private ActionRepository actionRepository;

    public void saveAction(Action action) {
        actionRepository.save(action);
    }
    
}
