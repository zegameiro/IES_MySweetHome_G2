package mysweethome.MSHbackend.Repositories;

import mysweethome.MSHbackend.Models.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends MongoRepository<Room, String> {


    @Query("{}")
    public List<Room> findAllRoom();
    public Room findByName(String name);
    public Room findByUid(String uid);

}
