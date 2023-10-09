package vn.techmaster.ecommecerapp.rest.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.LoginRequest;
import vn.techmaster.ecommecerapp.model.request.RegisterRequest;
import vn.techmaster.ecommecerapp.model.request.ResetPasswordRequest;
import vn.techmaster.ecommecerapp.service.AuthService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthResources {
    private final AuthService authService;

    @PostMapping("/public/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        authService.login(request, session);
        return ResponseEntity.ok("Đăng nhập thành công");
    }

    // create post method for register
    @PostMapping("/public/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/public/auth/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email) {
        authService.resetPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/public/auth/change-password")
    public ResponseEntity<?> confirmResetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/auth/login")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginRequest request, HttpSession session) {
        authService.loginAdmin(request, session);
        return ResponseEntity.ok("Đăng nhập thành công");
    }
}
