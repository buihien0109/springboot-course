package vn.techmaster.demoauthsessioncookie.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.techmaster.demoauthsessioncookie.constant.ConstantValue;
import vn.techmaster.demoauthsessioncookie.entity.User;
import vn.techmaster.demoauthsessioncookie.repository.UserRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthUtils {
    private final UserRepository userRepository;

    // Kiểm tra xem người dùng đã đăng nhập hay chưa
    public boolean isLogined(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(ConstantValue.COOKIE_NAME)) {
                    Integer userId = Integer.valueOf(cookie.getValue());
                    Optional<User> user = userRepository.findById(userId);
                    return user.isPresent();
                }
            }
        }
        return false;
    }

    // Lấy thông tin người dùng đã đăng nhập
    // Đây là giải pháp bảo mật hơn
    // https://stackoverflow.com/questions/4773609/what-is-a-relatively-secure-way-of-using-a-login-cookie
    public User getLoginedUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().equals(ConstantValue.COOKIE_NAME)) {
                    Integer userId = Integer.valueOf(cookie.getValue());
                    Optional<User> optionalUser = userRepository.findById(userId);
                    return optionalUser.orElse(null);
                }
            }
        }
        return null;
    }

    // Lưu thông tin người dùng vào cookie
    public void setLoginedCookie(HttpServletResponse response, User user) {
        Cookie loginCookie = new Cookie(ConstantValue.COOKIE_NAME, String.valueOf(user.getId()));
        loginCookie.setMaxAge(30 * 60);
        loginCookie.setPath("/");
        response.addCookie(loginCookie);
    }

    // Xóa cookie đã lưu thông tin người dùng
    public void clearLoginedCookie(HttpServletResponse response) {
        Cookie loginCookie = new Cookie(ConstantValue.COOKIE_NAME, null);
        loginCookie.setMaxAge(0);
        loginCookie.setHttpOnly(true);
        loginCookie.setPath("/");
        // add cookie to response
        response.addCookie(loginCookie);
    }
}
