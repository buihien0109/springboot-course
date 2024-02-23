package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.ecommecerapp.constant.ConstantValue;
import vn.techmaster.ecommecerapp.entity.Role;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.entity.UserAddress;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.dto.UserNormalDto;
import vn.techmaster.ecommecerapp.model.projection.UserAddressPublic;
import vn.techmaster.ecommecerapp.model.projection.UserPublic;
import vn.techmaster.ecommecerapp.model.request.CreateUserRequest;
import vn.techmaster.ecommecerapp.model.request.UpdatePasswordRequest;
import vn.techmaster.ecommecerapp.model.request.UpdateProfileUserRequest;
import vn.techmaster.ecommecerapp.model.request.UpdateUserRequest;
import vn.techmaster.ecommecerapp.model.response.ImageResponse;
import vn.techmaster.ecommecerapp.repository.RoleRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;
import vn.techmaster.ecommecerapp.utils.StringUtils;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FileServerService fileServerService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserAddressService userAddressService;

    // get all user return list userpublic
    public List<UserPublic> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .sorted((u1, u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt()))
                .map(UserPublic::of).toList();
    }

    public List<UserNormalDto> getAllAvailabelUsersNormalDtoByAdmin() {
        return userRepository.getAllAvailabelUsersNormalDtoByAdmin();
    }

    public List<UserNormalDto> getAllAvailabelByRole(String roleName) {
        return userRepository.getAllAvailabelByRole(roleName);
    }

    // get user by id
    public UserPublic getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy user"));
        return UserPublic.of(user);
    }

    // create method for save user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public String updateAvatar(MultipartFile file) {
        User user = SecurityUtils.getCurrentUserLogin();
        ImageResponse imageResponse = fileServerService.uploadFile(file);
        user.setAvatar(imageResponse.getUrl());
        userRepository.save(user);
        return user.getAvatar();
    }

    public String updateAvatar(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy user"));

        ImageResponse imageResponse = fileServerService.uploadFile(file);
        user.setAvatar(imageResponse.getUrl());
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

    public UserPublic createUser(CreateUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email đã tồn tại");
        }

        // List Role
        Set<Role> roleList = roleRepository.findByRoleIdIn(request.getRoleIds());

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(ConstantValue.PASSWORD_DEFAULT))
                .avatar(StringUtils.generateLinkImage(request.getUsername()))
                .enabled(true)
                .roles(roleList)
                .build();

        userRepository.save(user);
        return UserPublic.of(user);
    }

    public UserPublic updateUserById(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy user"));

        // List Role
        Set<Role> roleList = roleRepository.findByRoleIdIn(request.getRoleIds());

        user.setUsername(request.getUsername());
        user.setPhone(request.getPhone());
        user.setRoles(roleList);

        userRepository.save(user);
        return UserPublic.of(user);
    }

    public String resetPassword(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy user"));

        user.setPassword(passwordEncoder.encode(ConstantValue.PASSWORD_DEFAULT));
        userRepository.save(user);
        return ConstantValue.PASSWORD_DEFAULT;
    }

    public List<UserAddressPublic> getAllAddress(Long id) {
        List<UserAddress> addressList = userAddressService.findAllAddressByUserId(id);
        return addressList.stream().map(UserAddressPublic::of).toList();
    }

    public void enableUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy user"));

        if (user.getEnabled()) {
            throw new BadRequestException("User đã được kích hoạt");
        }

        user.setEnabled(true);
        userRepository.save(user);
    }
}
