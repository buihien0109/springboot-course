package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<User> {
    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        log.info("getPrincipal : {}", authentication.getPrincipal());
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("customUserDetails : {}", customUserDetails);
        log.info("username : {}", customUserDetails.getUsername());
        return Optional.ofNullable(customUserDetails.getUser());
    }
}
