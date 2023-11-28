package com.example.demo.thymeleaf.crud.controller;

import com.example.demo.thymeleaf.crud.entity.User;
import com.example.demo.thymeleaf.crud.request.LoginData;
import com.example.demo.thymeleaf.crud.request.RegistrationData;
import com.example.demo.thymeleaf.crud.service.AuthService;
import com.example.demo.thymeleaf.crud.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/";
        }
        model.addAttribute("loginData", new LoginData());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginData loginData, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "auth/login";
        }

        User user = authService.authenticate(loginData);
        if (user != null) {
            // Đăng nhập thành công, lưu thông tin người dùng vào session
            session.setAttribute("currentUser", user);
            return "redirect:/";
        } else {
            // Đăng nhập thất bại, thêm thông báo lỗi
            result.rejectValue("password", "login.failed", "Tên đăng nhập hoặc mật khẩu không đúng");
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa session
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model, HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/";
        }
        model.addAttribute("registrationData", new RegistrationData());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registrationData") @Valid RegistrationData registrationData, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        // Kiểm tra xem người dùng đã tồn tại hay chưa
        if (userService.existsByEmail(registrationData.getEmail())) {
            result.rejectValue("password", "register.failed", "Email đã được sử dụng");
            return "auth/register";
        }

        // Tạo và lưu người dùng mới
        userService.register(registrationData);

        // Chuyển hướng người dùng đến trang đăng nhập hoặc trang xác nhận
        return "redirect:/login";
    }
}
