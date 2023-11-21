package mysweethome.MSHbackend.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mysweethome.MSHbackend.Repositories.UserRepository;
import mysweethome.MSHbackend.Models.User;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByID(String id) {
        return userRepository.findByUid(id);
    }

    public User findByName(String firstname, String lastname) {
        return userRepository.findByFirstnameAndLastname(firstname, lastname);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByEmailAndPassword(String name, String password) {
        return userRepository.findByNameAndPassword(name, password);
    }
   
}
