package mysweethome.MSHbackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mysweethome.MSHbackend.Repositories.AlertRepository;
import mysweethome.MSHbackend.Models.Alert;
import java.util.LinkedList;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;
    
    public void saveAlert(Alert alert) {
        alertRepository.save(alert);
    }

    public Alert findByID(String id) {
        return alertRepository.findByAlertId(id);
    }

    public LinkedList<Alert> getAll() {
        return alertRepository.getAll();
    }

}
