package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Kiểm tra quyền của user đối với moi request
@Slf4j
@Component
@AllArgsConstructor
public class CustomFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // get header from request
        String authHeader = request.getHeader("Authorization");
        log.info("authHeader : {}", authHeader);

        // check header has Bearer token or not
        if (authHeader == null || !authHeader.contains("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // get jwt token from header
        String jwtToken = authHeader.substring(7);
        log.info("jwtToken : {}", jwtToken);

        // get email from jwt token
        String userEmail = jwtUtils.extractUsername(jwtToken);
        log.info("userEmail : {}", userEmail);

        // check email is not null and security context has not been set
        if (userEmail != null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
            log.info("userDetails : {}", userDetails);
            log.info("userDetails.getAuthorities() : {}", userDetails.getAuthorities());
            log.info("userDetails.getUsername() : {}", userDetails.getUsername());

            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                log.info("jwt token is valid");
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // continue filter
        filterChain.doFilter(request, response);
    }
}
