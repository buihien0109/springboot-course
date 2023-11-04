package com.example.demo.user.management.dao;

import com.example.demo.user.management.database.UserDB;
import com.example.demo.user.management.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDAOImpl implements UserDAO {
    @Override
    public List<User> findAll() {
        return UserDB.userList;
    }

    @Override
    public List<User> findByNameContainingIgnoreCase(String name) {
        return UserDB.userList.stream()
                .filter(user -> user.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return UserDB.userList.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return UserDB.userList.stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public User save(User user) {
        Optional<User> userOptional = findById(user.getId());
        if (userOptional.isPresent()) {
            User userUpdate = userOptional.get();
            userUpdate.setName(user.getName());
            userUpdate.setEmail(user.getEmail());
            userUpdate.setPhone(user.getPhone());
            userUpdate.setAddress(user.getAddress());
            userUpdate.setAvatar(user.getAvatar());
            userUpdate.setPassword(user.getPassword());
            return userUpdate;
        } else {
            user.setId(createId());
            UserDB.userList.add(user);
            return user;
        }
    }

    @Override
    public void deleteById(Integer id) {
        UserDB.userList.removeIf(user -> user.getId().equals(id));
    }

    private Integer createId() {
        List<User> userList = UserDB.userList;
        return userList.stream()
                .map(User::getId)
                .mapToInt(Integer::valueOf)
                .max().orElse(0) + 1;
    }
}
