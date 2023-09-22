package vn.techmaster.ecommecerapp.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import vn.techmaster.ecommecerapp.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SecurityUtils {
    // Get the login of the current user.
    public static User getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Optional<User> userOptional = Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof UserDetails) {
                CustomUserDetails springSecurityUser = (CustomUserDetails) authentication.getPrincipal();
                return springSecurityUser.getUser();
            }
            return null;
        });
        return userOptional.orElse(null);
    }

    // Check if a user is authenticated.
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
            log.info("authorities = {}", authorities);
            return authorities.stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("USER"));
        }).orElse(false);
    }
}
