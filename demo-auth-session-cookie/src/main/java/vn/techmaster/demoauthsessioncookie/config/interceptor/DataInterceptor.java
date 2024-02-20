package vn.techmaster.demoauthsessioncookie.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import vn.techmaster.demoauthsessioncookie.auth.AuthUtils;
import vn.techmaster.demoauthsessioncookie.entity.User;

@Component
@RequiredArgsConstructor
public class DataInterceptor implements HandlerInterceptor {
    private final AuthUtils authUtils;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Lấy thông tin người dùng từ trong cookie
        User user = authUtils.getLoginedUser(request);

        if (user != null) {
            modelAndView.addObject("userName", user.getName());
            modelAndView.addObject("isLogined", true);
        } else {
            modelAndView.addObject("isLogined", false);
        }
    }
}
