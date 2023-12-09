package com.example.demo.application.event.service;

import com.example.demo.application.event.entity.User;
import com.example.demo.application.event.event.type.user.UserCreatedEvent;
import com.example.demo.application.event.event.type.user.UserDeletedEvent;
import com.example.demo.application.event.event.type.user.UserUpdatedEvent;
import com.example.demo.application.event.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        user.addEvent(new UserCreatedEvent(user.getId()));
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        user.addEvent(new UserUpdatedEvent(user.getId()));
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.addEvent(new UserDeletedEvent(id));
        userRepository.deleteById(id);
    }
}
