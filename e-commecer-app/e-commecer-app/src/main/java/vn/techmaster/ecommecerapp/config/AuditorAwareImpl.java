package vn.techmaster.ecommecerapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.security.CustomUserDetails;

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
