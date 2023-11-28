package com.example.demo.thymeleaf.crud.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;
    private final RoleBasedAuthInterceptor roleBasedAuthInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/admin/**", "/cart") // Áp dụng cho tất cả các URL
                .excludePathPatterns("/login", "/register", "/css/**", "/js/**"); // Trừ các URL không cần kiểm tra
        registry.addInterceptor(roleBasedAuthInterceptor)
                .addPathPatterns("/admin", "/admin/users/**", "/admin/products/**")
                .excludePathPatterns("/login", "/register", "/css/**", "/js/**"); // Trừ các URL không cần kiểm tra;
    }
}
