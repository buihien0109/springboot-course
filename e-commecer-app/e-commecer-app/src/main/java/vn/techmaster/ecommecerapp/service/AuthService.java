package vn.techmaster.ecommecerapp.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

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
        } catch (Exception e) {
            throw new RuntimeException("Login fail");
        }
    }

    // create method for register
    public void register(RegisterRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new BadRequestException("Email already exist");
        }

        // get role by role name
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResouceNotFoundException("Role not found"));

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRoles(Set.of(role));
        userRepository.save(newUser);
    }
}
