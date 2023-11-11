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

    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    
}
