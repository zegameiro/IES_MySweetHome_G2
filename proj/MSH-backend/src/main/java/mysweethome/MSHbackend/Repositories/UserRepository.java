package mysweethome.MSHbackend.Repositories;

import mysweethome.MSHbackend.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends MongoRepository<User, String> {


    public User findByName(String name);
    public User findByID(String id);
    
    @Query("{'email' : :#{#email}, 'password' : :#{#pass}}")
    public User findByNameAndPassword(@Param("email") String email, @Param("pass") String pass);

}
