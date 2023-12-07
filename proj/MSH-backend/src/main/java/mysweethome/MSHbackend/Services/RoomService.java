package mysweethome.MSHbackend.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mysweethome.MSHbackend.Repositories.RoomRepository;
import mysweethome.MSHbackend.Models.Room;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public void saveRoom(Room room) {
        roomRepository.save(room);
    }

    public Room findByName(String name) {
        return roomRepository.findByName(name);
    }

    public List<Room> findAllRoom() {
        return roomRepository.findAllRoom();
    }

    public Room findByID(String id) {
        return roomRepository.findByID(id);
    }
   
}
