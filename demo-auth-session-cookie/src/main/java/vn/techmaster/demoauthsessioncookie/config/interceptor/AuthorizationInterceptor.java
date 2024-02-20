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
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final AuthUtils authUtils;
    private final ObjectMapper objectMapper; // Inject ObjectMapper


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Lấy thông tin người dùng từ trong cookie
        User user = authUtils.getLoginedUser(request);
        String role = user.getRole().getRole();
        String requestURI = request.getRequestURI();

        // Nếu path có prefix = "/api/admin/**" mà role của user không phải là ROLE_ADMIN thì báo lỗi 403 (forbidden)
        if (requestURI.startsWith("/api/admin") && !role.equals("ROLE_ADMIN")) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập");

            // Convert ErrorResponse to JSON string using Jackson
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);

            // Write JSON response to the HttpServletResponse
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
            response.getWriter().close();

            return false;
        }

        // Nếu path có prefix = "/admin/**" mà role của user không phải là ROLE_ADMIN thì chuyển hướng sang trang /dang-nhap
        if (requestURI.startsWith("/admin") && !role.equals("ROLE_ADMIN")) {
            response.sendRedirect("/dang-nhap");
            return false;
        }

        return true;
    }
}
