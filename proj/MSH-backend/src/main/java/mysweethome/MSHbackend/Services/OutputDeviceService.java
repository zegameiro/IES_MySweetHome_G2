package mysweethome.MSHbackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mysweethome.MSHbackend.Repositories.OutputDeviceRepository;
import mysweethome.MSHbackend.Models.OutputDevice;
import java.util.LinkedList;

@Service
public class OutputDeviceService {

    @Autowired
    private OutputDeviceRepository outDevRepository;

    public void saveOutputDevice(OutputDevice outDev) {
        outDevRepository.save(outDev);
    }

    public OutputDevice saveGetOutputDevice(OutputDevice outDev) {
        return outDevRepository.save(outDev);
    }

    public OutputDevice findByID(String id) {
        return outDevRepository.findByID(id);
    }

    public LinkedList<OutputDevice> getAll() {
        return outDevRepository.getAll();
    }

}
