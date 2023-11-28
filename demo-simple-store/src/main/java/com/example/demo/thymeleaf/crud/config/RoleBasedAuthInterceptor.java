package com.example.demo.thymeleaf.crud.config;

import com.example.demo.thymeleaf.crud.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
@Slf4j
public class RoleBasedAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("RoleBasedAuthInterceptor.preHandle");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        String role = user.getRole();
        log.info("role: {}", role);

        String requestURI = request.getRequestURI();
        log.info("requestURI: {}", requestURI);

        if (requestURI.startsWith("/admin/users") && !role.equals("ROLE_ADMIN")) {
            response.sendRedirect("/admin/unauthorized");
            return false;
        }

        if (requestURI.startsWith("/admin") && !List.of("ROLE_ADMIN", "ROLE_SALE").contains(role)) {
            response.sendRedirect("/admin/unauthorized");
            return false;
        }

        return true;
    }
}
