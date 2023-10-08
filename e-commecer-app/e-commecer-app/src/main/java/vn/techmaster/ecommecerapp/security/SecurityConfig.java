package vn.techmaster.ecommecerapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vn.techmaster.ecommecerapp.security.error.CustomAccessDeniedHandler;
import vn.techmaster.ecommecerapp.security.error.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final AuthenticationProvider authenticationProvider;
    private final CustomFilter customFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/admin-assets/**").permitAll();
            auth.requestMatchers("/", "/danh-muc/**", "/san-pham/**", "/gio-hang", "/yeu-thich", "/bai-viet", "/bai-viet/**", "/thanh-toan", "/dang-nhap", "/dang-ky", "/quen-mat-khau", "/xac-nhan-don-hang/**").permitAll();
            auth.requestMatchers("/api/v1/public/**").permitAll();
            auth.requestMatchers("/admin/login", "/api/v1/admin/auth/login").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/api/v1/files/{id}").permitAll();
            auth.requestMatchers("/admin/**", "/api/v1/admin/**").hasRole("ADMIN");
            auth.requestMatchers("/khach-hang/**").hasAnyRole("ADMIN", "USER");
            auth.requestMatchers("/api/v1/users/**").hasAnyRole("ADMIN", "USER");
            auth.requestMatchers(HttpMethod.POST, "/api/v1/files").hasAnyRole("ADMIN", "USER");
            auth.requestMatchers(HttpMethod.PUT, "/api/v1/files/{id}").hasAnyRole("ADMIN", "USER");
            auth.requestMatchers(HttpMethod.DELETE, "/api/v1/files/{id}").hasAnyRole("ADMIN", "USER");
            auth.anyRequest().authenticated();
        });
        http.logout(logout -> {
            logout.deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .permitAll()
                    .clearAuthentication(true)
                    .logoutSuccessHandler((request, response, authentication) -> {
                        request.getSession().setAttribute("MY_SESSION", null);
                        request.getSession().invalidate();
                        response.setStatus(HttpStatus.OK.value());
                    });
        });
        http.exceptionHandling(exceptionHandling -> {
            exceptionHandling.accessDeniedHandler(customAccessDeniedHandler);
            exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint);
        });
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers(headers -> {
            headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
        });
        return http.build();
    }
}
