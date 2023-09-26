package vn.techmaster.ecommecerapp.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.service.UserAddressService;

@Controller
@RequestMapping("/khach-hang")
public class UserController {
    private final UserAddressService userAddressService;

    public UserController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @GetMapping("/tai-khoan")
    public String account() {
        return "user/account";
    }

    @GetMapping("/dia-chi")
    public String address(Model model) {
        model.addAttribute("addressList", userAddressService.findAllAddressByUserLogin());
        return "user/address";
    }
}
