package vn.techmaster.demoauthsessioncookie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.demoauthsessioncookie.service.UserService;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping
    public String getAllUsers(Model model) {
        model.addAttribute("userList", userService.getAllUsers());
        return "admin/user/index";
    }
}
