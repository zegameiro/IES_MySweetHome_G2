package mysweethome.MSHbackend.Repositories;

import mysweethome.MSHbackend.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends MongoRepository<User, String> {


    @Query("{'firstname' : :#{#fname}, 'lastname' : :#{#lname}}")
    public User findByFirstnameAndLastname(@Param("fname") String fname, @Param("lname") String lname);

    public User findByUid(String uid);
    
    @Query("{'email' : :#{#email}, 'password' : :#{#pass}}")
    public User findByNameAndPassword(@Param("email") String email, @Param("pass") String pass);

    @Query("{'email' : :#{#email}}")
    public User findByEmail(@Param("email") String email);

}
