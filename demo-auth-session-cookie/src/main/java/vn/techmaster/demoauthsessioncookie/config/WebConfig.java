package vn.techmaster.demoauthsessioncookie.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.techmaster.demoauthsessioncookie.config.interceptor.AuthenticationInterceptor;
import vn.techmaster.demoauthsessioncookie.config.interceptor.AuthorizationInterceptor;
import vn.techmaster.demoauthsessioncookie.config.interceptor.DataInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthorizationInterceptor authorizationInterceptor;
    private final DataInterceptor dataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/api/**", "/api/admin/**", "/web/css/**", "/web/js/**");  // Không áp dụng cho các request tới các đường dẫn bên dưới
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/reviews/**", "/user/profile", "/admin/**", "/api/admin/**");  // Chỉ áp dụng cho các request tới các đường dẫn bên dưới
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/admin/**", "/api/admin/**");  // Chỉ áp dụng cho các request tới các đường dẫn bên dưới
    }
}
