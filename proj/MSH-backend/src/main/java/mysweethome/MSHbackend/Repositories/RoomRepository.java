package mysweethome.MSHbackend.Repositories;

import mysweethome.MSHbackend.Models.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RoomRepository extends MongoRepository<Room, String> {


    @Query("{}")
    public List<Room> findAllRoom();
    public Room findByName(String name);
    @Query("{'id': ?0}")
    public Room findByID(String id);

}
