package mysweethome.MSHbackend.Repositories;
import mysweethome.MSHbackend.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{ 'username' : ?0 }")
    User findByUsername(String username);

    public User findByName(String name);

    
}
