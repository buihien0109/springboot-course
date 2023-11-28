package com.example.demo.thymeleaf.crud.service;

import com.example.demo.thymeleaf.crud.entity.User;
import com.example.demo.thymeleaf.crud.repository.UserRepository;
import com.example.demo.thymeleaf.crud.request.RegistrationData;
import com.example.demo.thymeleaf.crud.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<User> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return userRepository.findAll(pageable);
    }

    public Page<User> findPaginatedWithSearch(String keyword, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.findByNameContaining(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }

    public void save(User user) {
        // encode password user before save to database
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void update(UpdateUserRequest user) {
        // get user from database
        User userFromDb = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + user.getId()));

        // update user
        userFromDb.setName(user.getName());
        userFromDb.setEmail(user.getEmail());
        userFromDb.setRole(user.getRole());

        // save user
        userRepository.save(userFromDb);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void register(RegistrationData registrationData) {
        User user = new User();
        user.setName(registrationData.getName());
        user.setEmail(registrationData.getEmail());
        user.setPassword(registrationData.getPassword());
        user.setRole("ROLE_USER");
        save(user);
    }
}
