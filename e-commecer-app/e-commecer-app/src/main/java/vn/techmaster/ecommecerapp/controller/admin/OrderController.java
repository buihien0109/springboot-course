package vn.techmaster.ecommecerapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.service.OrderService;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getOrderPage(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/order/index";
    }

    @GetMapping("/{id}/detail")
    public String getOrderDetailPage(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "admin/order/detail";
    }
}
