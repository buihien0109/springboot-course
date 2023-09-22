package vn.techmaster.ecommecerapp.service;

import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // create method for save user
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
