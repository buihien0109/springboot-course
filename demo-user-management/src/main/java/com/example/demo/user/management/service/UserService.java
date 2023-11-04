package com.example.demo.user.management.service;

import com.example.demo.user.management.dto.UserDto;
import com.example.demo.user.management.request.CreateUserRequest;
import com.example.demo.user.management.request.UpdateAvatarRequest;
import com.example.demo.user.management.request.UpdatePasswordRequest;
import com.example.demo.user.management.request.UpdateUserRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    List<UserDto> searchUser(String name);

    UserDto getUserById(Integer id);

    void deleteUser(Integer id);

    UserDto createUser(CreateUserRequest request);

    UserDto updateUser(Integer id, UpdateUserRequest request);

    void updatePassword(Integer id, UpdatePasswordRequest request);

    String forgotPassword(Integer id);

    void updateAvatar(Integer id, UpdateAvatarRequest request);

    String uploadFile(Integer id, MultipartFile file);

    byte[] readFile(Integer id, String fileName);

    List<String> getFiles(Integer id);

    void deleteFile(int id, String fileName);
}
