package vn.techmaster.ecommecerapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.model.request.UpdatePasswordRequest;
import vn.techmaster.ecommecerapp.model.request.UpdateProfileUserRequest;
import vn.techmaster.ecommecerapp.repository.UserRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;

import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FileServerService fileServerService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, FileServerService fileServerService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.fileServerService = fileServerService;
        this.passwordEncoder = passwordEncoder;
    }

    // create method for save user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public String updateAvatar(MultipartFile file) {
        User user = SecurityUtils.getCurrentUserLogin();
        Map<String, Long> data = fileServerService.uploadFile(file);
        String avatar = "/api/v1/files/" + data.get("id");
        user.setAvatar(avatar);
        userRepository.save(user);
        return user.getAvatar();
    }

    public void updateProfile(UpdateProfileUserRequest request) {
        User user = SecurityUtils.getCurrentUserLogin();
        user.setUsername(request.getUsername());
        user.setPhone(request.getPhone());

        userRepository.save(user);
    }

    public void updatePassword(UpdatePasswordRequest request) {
        User user = SecurityUtils.getCurrentUserLogin();

        // check old password correct
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Mật khẩu cũ không đúng");
        }

        // check new password and confirm password match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }

        // check new password and old password match
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("Mật khẩu mới không được trùng với mật khẩu cũ");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
