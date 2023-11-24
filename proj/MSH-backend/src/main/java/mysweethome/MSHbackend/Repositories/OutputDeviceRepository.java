package mysweethome.MSHbackend.Repositories;
import mysweethome.MSHbackend.Models.OutputDevice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.LinkedList;


public interface OutputDeviceRepository extends MongoRepository<OutputDevice, String> {

    // adicionar + cenas conforme for preciso
    @Query("{}")
    public LinkedList<OutputDevice> getAll();

    public OutputDevice findByID(String id);
}
