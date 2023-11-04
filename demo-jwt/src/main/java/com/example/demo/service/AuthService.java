package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.request.LoginRequest;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtils = jwtUtils;
    }

    // handle login
    public String login(LoginRequest request) {
        // create authentication object
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        try {
            // Process authentication
            Authentication authentication = authenticationManager.authenticate(token);

            // Save authentication object to SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get user details
            UserDetails userDetails = customUserDetailsService
                    .loadUserByUsername(authentication.getName());

            // Create JWT token and return
            return jwtUtils.generateToken(userDetails);
        } catch (Exception e) {
            log.error("Error while login: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }
}
