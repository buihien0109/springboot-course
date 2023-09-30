package vn.techmaster.ecommecerapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.service.OrderService;
import vn.techmaster.ecommecerapp.service.RoleService;
import vn.techmaster.ecommecerapp.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final OrderService orderService;

    public UserController(UserService userService, RoleService roleService, OrderService orderService) {
        this.userService = userService;
        this.roleService = roleService;
        this.orderService = orderService;
    }

    // Danh sách tất cả user
    @GetMapping
    public String getUserPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/user/index";
    }

    // Tạo user
    @GetMapping("/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/user/create";
    }

    // Chi tiết user
    @GetMapping("/{id}/detail")
    public String getUserDetailPage(@PathVariable Long id, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("orders", orderService.getOrdersByUserId(id));

        return "admin/user/detail";
    }
}
