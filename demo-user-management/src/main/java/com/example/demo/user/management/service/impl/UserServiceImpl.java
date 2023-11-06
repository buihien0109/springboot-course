package com.example.demo.user.management.service.impl;

import com.example.demo.user.management.dao.UserDAO;
import com.example.demo.user.management.dto.UserDto;
import com.example.demo.user.management.dto.mapper.UserMapper;
import com.example.demo.user.management.exception.BadRequestException;
import com.example.demo.user.management.exception.ResourceNotFoundException;
import com.example.demo.user.management.model.User;
import com.example.demo.user.management.request.CreateUserRequest;
import com.example.demo.user.management.request.UpdateAvatarRequest;
import com.example.demo.user.management.request.UpdatePasswordRequest;
import com.example.demo.user.management.request.UpdateUserRequest;
import com.example.demo.user.management.service.FileService;
import com.example.demo.user.management.service.UserService;
import com.example.demo.user.management.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final FileService fileService;

    @Override
    public List<UserDto> getUsers() {
        return userDAO.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> searchUser(String name) {
        return userDAO.findByNameContainingIgnoreCase(name)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + id));
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + id));
        userDAO.deleteById(user.getId());
        fileService.deleteAllFiles(id);
    }

    @Override
    public UserDto createUser(CreateUserRequest request) {
        if (userDAO.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("email = " + request.getEmail() + " đã tồn tại");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .password(request.getPassword())
                .build();

        userDAO.save(user);

        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Integer id, UpdateUserRequest request) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + id));

        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setAvatar(request.getAvatar());

        userDAO.save(user);

        return UserMapper.toUserDto(user);
    }

    @Override
    public void updatePassword(Integer id, UpdatePasswordRequest request) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + id));

        // Kiểm tra oldPassword có đúng không
        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new BadRequestException("Mật khẩu cũ không đúng!");
        }

        // Kiểm tra oldPassword có = newPassword không
        if (request.getNewPassword().equals(request.getOldPassword())) {
            throw new BadRequestException("Mật khẩu mới không được trùng với mật khẩu cũ!");
        }

        // Cập nhật newPassword cho user tương ứng
        user.setPassword(request.getNewPassword());
        userDAO.save(user);
    }

    @Override
    public String forgotPassword(Integer id) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + id));

        // Random chuỗi password mới cho user
        String newPassword = Utils.generatePassword(3);

        // Lấy thông tin của user và đặt lại password mới cho user
        user.setPassword(newPassword);
        userDAO.save(user);

        // Trả về thông tin password mới
        return newPassword;
    }

    @Override
    public void updateAvatar(Integer id, UpdateAvatarRequest request) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + id));

        // Update lại avatar
        user.setAvatar(request.getAvatar());
        userDAO.save(user);
    }

    @Override
    public String uploadFile(Integer id, MultipartFile file) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + id));

        return fileService.uploadFile(id, file);
    }

    @Override
    public byte[] readFile(Integer id, String fileName) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + id));

        return fileService.readFile(id, fileName);
    }

    @Override
    public List<String> getFiles(Integer id) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + id));

        return fileService.getFiles(id);
    }

    @Override
    public void deleteFile(int id, String fileName) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + id));

        fileService.deleteFile(id, fileName);
    }
}
