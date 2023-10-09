package vn.techmaster.ecommecerapp.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.techmaster.ecommecerapp.entity.Role;
import vn.techmaster.ecommecerapp.entity.TokenConfirm;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.request.LoginRequest;
import vn.techmaster.ecommecerapp.model.request.RegisterRequest;
import vn.techmaster.ecommecerapp.model.request.ResetPasswordRequest;
import vn.techmaster.ecommecerapp.repository.RoleRepository;
import vn.techmaster.ecommecerapp.repository.TokenConfirmRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final TokenConfirmRepository tokenConfirmRepository;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final MailService mailService;

    // create method for login
    public void login(LoginRequest request, HttpSession session) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("MY_SESSION", authentication.getName());
        } catch (AuthenticationException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    // create method for login admin
    public void loginAdmin(LoginRequest request, HttpSession session) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        try {
            Authentication authentication = authenticationManager.authenticate(token);
            log.info("authentication: {}", authentication);
            log.info("authentication.getAuthorities(): {}", authentication.getAuthorities());

            // check role admin
            if (authentication.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
                throw new BadRequestException("Bạn không có quyền truy cập");
            } else {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                session.setAttribute("MY_SESSION", authentication.getName());
            }
        } catch (AuthenticationException e) {
            // log type of exception
            log.error("Exception: {}", e.getClass());
            throw new BadRequestException(e.getMessage());
        }
    }

    // create method for register
    @Transactional
    public void register(RegisterRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new BadRequestException("Email đã tồn tại");
        }

        // get role by role name
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy role"));

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEnabled(false);
        newUser.setRoles(Set.of(role));
        userRepository.save(newUser);

        // Create token confirm
        TokenConfirm tokenConfirm = new TokenConfirm();
        tokenConfirm.setToken(UUID.randomUUID().toString());
        tokenConfirm.setUser(newUser);
        tokenConfirm.setTokenType(TokenConfirm.TokenType.EMAIL_VERIFICATION);
        // set expiry date after 1 day
        tokenConfirm.setExpiryDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        tokenConfirmRepository.save(tokenConfirm);

        // send email
        Map<String, String> data = new HashMap<>();
        data.put("email", newUser.getEmail());
        data.put("username", newUser.getUsername());
        data.put("token", tokenConfirm.getToken());
        mailService.sendMailConfirmRegistration(data);
    }

    @Transactional
    public Map<String, Object> confirmEmail(String token) {
        Map<String, Object> data = new HashMap<>();
        Optional<TokenConfirm> tokenConfirmOptional = tokenConfirmRepository
                .findByTokenAndTokenType(token, TokenConfirm.TokenType.EMAIL_VERIFICATION);
        if (tokenConfirmOptional.isEmpty()) {
            data.put("success", false);
            data.put("message", "Token xác thực tài khoản không hợp lệ");
            return data;
        }

        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
        if (tokenConfirm.getConfirmedDate() != null) {
            data.put("success", false);
            data.put("message", "Token xác thực tài khoản đã được xác nhận");
            return data;
        }

        if (tokenConfirm.getExpiryDate().before(new Date())) {
            data.put("success", false);
            data.put("message", "Token xác thực tài khoản đã hết hạn");
            return data;
        }

        User user = tokenConfirm.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenConfirm.setConfirmedDate(new Date());
        tokenConfirmRepository.save(tokenConfirm);

        data.put("success", true);
        data.put("message", "Xác thực tài khoản thành công");
        return data;
    }

    public void resetPassword(String email) {
        log.info("email: {}", email);
        // check email exist
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy email"));
        log.info("user: {}", user);
        log.info("user.getEmail(): {}", user.getEmail());

        // Create token confirm
        log.info("Create token confirm");
        TokenConfirm tokenConfirm = new TokenConfirm();
        tokenConfirm.setToken(UUID.randomUUID().toString());
        tokenConfirm.setUser(user);
        tokenConfirm.setTokenType(TokenConfirm.TokenType.PASSWORD_RESET);
        // set expiry date after 1 day
        tokenConfirm.setExpiryDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        tokenConfirmRepository.save(tokenConfirm);

        // send email
        log.info("Send email");
        Map<String, String> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("username", user.getUsername());
        data.put("token", tokenConfirm.getToken());

        mailService.sendMailResetPassword(data);

        log.info("Send mail success");
    }

    @Transactional
    public void changePassword(ResetPasswordRequest request) {
        // check new password and confirm password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Mật khẩu mới và mật khẩu xác nhận không khớp");
        }

        // get token confirm
        Optional<TokenConfirm> tokenConfirmOptional = tokenConfirmRepository
                .findByTokenAndTokenType(request.getToken(), TokenConfirm.TokenType.PASSWORD_RESET);

        if (tokenConfirmOptional.isEmpty()) {
            throw new BadRequestException("Token đặt lại mật khẩu không hợp lệ");
        }

        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
        if (tokenConfirm.getConfirmedDate() != null) {
            throw new BadRequestException("Token đặt lại mật khẩu đã được xác nhận");
        }

        if (tokenConfirm.getExpiryDate().before(new Date())) {
            throw new BadRequestException("Token đặt lại mật khẩu đã hết hạn");
        }

        User user = tokenConfirm.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        tokenConfirm.setConfirmedDate(new Date());
        tokenConfirmRepository.save(tokenConfirm);

        log.info("Đặt lại mật khẩu thành công");
    }

    @Transactional
    public Map<String, Object> confirmResetPassword(String token) {
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);

        Optional<TokenConfirm> tokenConfirmOptional = tokenConfirmRepository
                .findByTokenAndTokenType(token, TokenConfirm.TokenType.PASSWORD_RESET);
        if (tokenConfirmOptional.isEmpty()) {
            data.put("success", false);
            data.put("message", "Token đặt lại mật khẩu không hợp lệ");
            return data;
        }

        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
        if (tokenConfirm.getConfirmedDate() != null) {
            data.put("success", false);
            data.put("message", "Token đặt lại mật khẩu đã được xác nhận");
            return data;
        }

        if (tokenConfirm.getExpiryDate().before(new Date())) {
            data.put("success", false);
            data.put("message", "Token đặt lại mật khẩu đã hết hạn");
            return data;
        }

        data.put("success", true);
        data.put("message", "Token đặt lại mật khẩu hợp lệ");
        return data;
    }
}
