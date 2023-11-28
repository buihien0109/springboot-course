package com.example.demo.thymeleaf.crud.service;

import com.example.demo.thymeleaf.crud.entity.User;
import com.example.demo.thymeleaf.crud.repository.UserRepository;
import com.example.demo.thymeleaf.crud.request.LoginData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User authenticate(LoginData loginData) {
        return userRepository.findByEmail(loginData.getEmail())
                .filter(user -> passwordEncoder.matches(loginData.getPassword(), user.getPassword()))
                .orElse(null);
    }
}
