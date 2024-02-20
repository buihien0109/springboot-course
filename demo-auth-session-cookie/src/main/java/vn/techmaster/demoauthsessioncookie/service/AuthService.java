package vn.techmaster.demoauthsessioncookie.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.techmaster.demoauthsessioncookie.auth.AuthUtils;
import vn.techmaster.demoauthsessioncookie.entity.User;
import vn.techmaster.demoauthsessioncookie.exception.BadRequestException;
import vn.techmaster.demoauthsessioncookie.exception.ResourceNotFoundException;
import vn.techmaster.demoauthsessioncookie.model.request.LoginRequest;
import vn.techmaster.demoauthsessioncookie.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthUtils authUtils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HttpServletResponse httpServletResponse;

    public void login(LoginRequest request) {
        // Kiểm tra xem user có tồn tại không
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email không tồn tại"));

        // Kiểm tra xem password có đúng không
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Mật khẩu không đúng");
        }

        // Lưu thông tin user vào cookie
        authUtils.setLoginedCookie(httpServletResponse, user);
    }

    public void logout() {
        // Xóa cookie
        authUtils.clearLoginedCookie(httpServletResponse);
    }
}
