package vn.techmaster.ecommecerapp.rest.web;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.ecommecerapp.model.request.UpdatePasswordRequest;
import vn.techmaster.ecommecerapp.model.request.UpdateProfileUserRequest;
import vn.techmaster.ecommecerapp.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserResources {

    private final UserService userService;

    public UserResources(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/update-avatar")
    public ResponseEntity<?> updateAvatar(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.updateAvatar(file));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileUserRequest request) {
        userService.updateProfile(request);
        return ResponseEntity.ok().body("Cập nhật thông tin thành công");
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(request);
        return ResponseEntity.ok().body("Cập nhật mật khẩu thành công");
    }
}
