package vn.techmaster.ecommecerapp.rest.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.techmaster.ecommecerapp.model.request.LoginRequest;
import vn.techmaster.ecommecerapp.model.request.RegisterRequest;
import vn.techmaster.ecommecerapp.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthResources {
    private final AuthService authService;

    public AuthResources(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        authService.login(request, session);
        return ResponseEntity.ok("Login success");
    }

    // create post method for register
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Register success");
    }
}
