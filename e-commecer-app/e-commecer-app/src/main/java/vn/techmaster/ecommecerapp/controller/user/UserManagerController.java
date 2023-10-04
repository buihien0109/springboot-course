package vn.techmaster.ecommecerapp.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.service.OrderService;
import vn.techmaster.ecommecerapp.service.UserAddressService;

@Controller
@RequestMapping("/khach-hang")
@RequiredArgsConstructor
public class UserManagerController {
    private final UserAddressService userAddressService;
    private final OrderService orderService;

    @GetMapping("/tai-khoan")
    public String account() {
        return "user/account";
    }

    @GetMapping("/dia-chi")
    public String address(Model model) {
        model.addAttribute("addressList", userAddressService.findAllAddressByUserLogin());
        return "user/address";
    }

    @GetMapping("/quan-ly-don-hang")
    public String order(Model model) {
        model.addAttribute("orders", orderService.getAllOrderWaitAndDeliveryByUserLogin());
        return "user/order";
    }

    @GetMapping("/lich-su-giao-dich")
    public String history(Model model) {
        model.addAttribute("orders", orderService.getOrderHistoryByUserLogin());
        return "user/history";
    }

    @GetMapping("/don-hang/{orderNumber}")
    public String orderDetail(Model model, @PathVariable String orderNumber) {
        model.addAttribute("order", orderService.getOrderByOrderNumber(orderNumber));
        return "user/order-detail";
    }
}
