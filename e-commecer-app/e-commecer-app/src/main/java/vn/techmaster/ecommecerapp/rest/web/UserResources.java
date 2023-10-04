package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.ecommecerapp.model.projection.UserPublic;
import vn.techmaster.ecommecerapp.model.request.CreateUserRequest;
import vn.techmaster.ecommecerapp.model.request.UpdateUserRequest;
import vn.techmaster.ecommecerapp.service.UserService;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserResources {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        UserPublic user = userService.createUser(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // Cập nhật thông tin user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        UserPublic user = userService.updateUserById(id, request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/update-avatar")
    public ResponseEntity<?> updateAvatar(@RequestParam("file") MultipartFile file, @PathVariable Long id) {
        return ResponseEntity.ok(userService.updateAvatar(id, file));
    }

    @PutMapping("/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id) {
        return ResponseEntity.ok(userService.resetPassword(id));
    }
}
