package vn.techmaster.ecommecerapp.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.service.OrderService;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public String getOrderPage(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/order/index";
    }

    @GetMapping("/{orderNumber}/detail")
    public String getOrderDetailPage(@PathVariable String orderNumber, Model model) {
        model.addAttribute("order", orderService.getOrderByOrderNumber(orderNumber));
        return "admin/order/detail";
    }
}
