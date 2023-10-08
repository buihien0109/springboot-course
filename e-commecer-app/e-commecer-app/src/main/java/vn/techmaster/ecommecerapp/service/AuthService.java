package vn.techmaster.ecommecerapp.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Role;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.request.LoginRequest;
import vn.techmaster.ecommecerapp.model.request.RegisterRequest;
import vn.techmaster.ecommecerapp.repository.RoleRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    // create method for login
    public void login(LoginRequest request, HttpSession session) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("MY_SESSION", authentication.getName());
        } catch (AuthenticationException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    // create method for login admin
    public void loginAdmin(LoginRequest request, HttpSession session) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        try {
            Authentication authentication = authenticationManager.authenticate(token);
            log.info("authentication: {}", authentication);
            log.info("authentication.getAuthorities(): {}", authentication.getAuthorities());

            // check role admin
            if (authentication.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
                throw new BadRequestException("Bạn không có quyền truy cập");
            } else {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                session.setAttribute("MY_SESSION", authentication.getName());
            }
        } catch (AuthenticationException e) {
            // log type of exception
            log.error("Exception: {}", e.getClass());
            throw new BadRequestException(e.getMessage());
        }
    }

    // create method for register
    public void register(RegisterRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new BadRequestException("Email đã tồn tại");
        }

        // get role by role name
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy role"));

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRoles(Set.of(role));
        userRepository.save(newUser);
    }
}
