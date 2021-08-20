package business;


import business.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import persistence.UserRepository;

import java.util.Optional;



@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder getEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerNewUserAccount(User user) {
        Optional<User> findUser = userRepository.findByEmail(user.getEmail());
        if (findUser.isPresent()) {
            return null;
        } else {
            user.setRoles("ROLE_USER");
            user.setActive(true);
            user.setPassword(getEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }


    public User save(User user) {
        return userRepository.save(user);
    }
}
