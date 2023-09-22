package vn.techmaster.ecommecerapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final CustomFilter customFilter;

    public SecurityConfig(AuthenticationProvider authenticationProvider, CustomFilter customFilter) {
        this.authenticationProvider = authenticationProvider;
        this.customFilter = customFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/css/**", "/js/**", "/img/**, /fonts/**").permitAll();
            auth.anyRequest().permitAll();
        });
        http.logout(logout -> {
            logout.deleteCookies("MYSESSION")
                    .invalidateHttpSession(true)
                    .permitAll();
        });
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers(headers -> {
            headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
        });
        return http.build();
    }

}