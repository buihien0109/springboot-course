package com.example.demo.user.management.rest;

import com.example.demo.user.management.dto.UserDto;
import com.example.demo.user.management.request.CreateUserRequest;
import com.example.demo.user.management.request.UpdateAvatarRequest;
import com.example.demo.user.management.request.UpdatePasswordRequest;
import com.example.demo.user.management.request.UpdateUserRequest;
import com.example.demo.user.management.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserResources {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers() {
        List<UserDto> userDtos = userService.getUsers();
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUser(@RequestParam String name) {
        List<UserDto> userDtos = userService.searchUser(name);
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        UserDto userDto = userService.createUser(request);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,
                                        @RequestBody UpdateUserRequest request) {
        UserDto userDto = userService.updateUser(id, request);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}/update-password")
    public ResponseEntity<?> updatePassword(@PathVariable Integer id,
                                            @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/forgot-password")
    public ResponseEntity<?> updatePassword(@PathVariable Integer id) {
        String password = userService.forgotPassword(id);
        return ResponseEntity.ok(password);
    }

    @PostMapping("/{id}/upload-file")
    public ResponseEntity<?> uploadFile(@PathVariable Integer id,
                                        @ModelAttribute("file") MultipartFile file) {
        String filePath = userService.uploadFile(id, file);
        return ResponseEntity.ok(filePath);
    }

    @GetMapping("/{id}/files/{fileName}")
    public ResponseEntity<?> readFile(@PathVariable Integer id, @PathVariable String fileName) {
        byte[] bytes = userService.readFile(id, fileName);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
    }

    @GetMapping("/{id}/files")
    public ResponseEntity<?> getFiles(@PathVariable Integer id) {
        List<String> paths = userService.getFiles(id);
        return ResponseEntity.ok(paths);
    }

    @PutMapping("/{id}/update-avatar")
    public ResponseEntity<?> updateAvatar(@PathVariable Integer id, @RequestBody UpdateAvatarRequest request) {
        userService.updateAvatar(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/files/{fileName}")
    public ResponseEntity<?> deleteFile(@PathVariable int id, @PathVariable String fileName) {
        userService.deleteFile(id, fileName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

