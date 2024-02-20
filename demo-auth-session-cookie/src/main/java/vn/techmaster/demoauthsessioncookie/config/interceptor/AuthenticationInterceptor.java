package vn.techmaster.demoauthsessioncookie.config.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.techmaster.demoauthsessioncookie.auth.AuthUtils;
import vn.techmaster.demoauthsessioncookie.entity.User;
import vn.techmaster.demoauthsessioncookie.model.response.ErrorResponse;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthUtils authUtils;
    private final ObjectMapper objectMapper; // Inject ObjectMapper


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Lấy thông tin người dùng từ trong cookie
        User user = authUtils.getLoginedUser(request);

        // Nếu currentUser không tồn tại hoặc = null hoặc path có prefix = "/api/**" thì báo lỗi 401 (unauthorized)
        if (user == null) {
            if (request.getRequestURI().startsWith("/api")) {
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Bạn chưa đăng nhập");

                // Convert ErrorResponse to JSON string using Jackson
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);

                // Write JSON response to the HttpServletResponse
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write(jsonResponse);
                response.getWriter().flush();
                response.getWriter().close();
            } else {
                response.sendRedirect("/dang-nhap");
            }
            return false;
        }

        return true;
    }
}
