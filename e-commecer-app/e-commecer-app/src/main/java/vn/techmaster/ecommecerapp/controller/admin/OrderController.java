package vn.techmaster.ecommecerapp.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.entity.OrderTable;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.service.CouponService;
import vn.techmaster.ecommecerapp.service.OrderService;
import vn.techmaster.ecommecerapp.service.ProductService;
import vn.techmaster.ecommecerapp.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;
    private final CouponService couponService;

    @GetMapping
    public String getOrderPage(Model model) {
        model.addAttribute("orders", orderService.getAllOrdersDtoAdmin());
        return "admin/order/index";
    }

    @GetMapping("/create")
    public String getOrderCreatePage(Model model) {
        model.addAttribute("products", productService.getAllAvailabelProductsAdminDtoByAdmin());
        model.addAttribute("paymentMethodList", OrderTable.PaymentMethod.values());
        model.addAttribute("shippingMethodList", OrderTable.ShippingMethod.values());
        model.addAttribute("users", userService.getAllAvailabelUsersNormalDtoByAdmin());
        model.addAttribute("coupons", couponService.getAllCouponValid());
        return "admin/order/create";
    }

    @GetMapping("/{orderNumber}/detail")
    public String getOrderDetailPage(@PathVariable String orderNumber, Model model) {
        model.addAttribute("products", productService.getAllAvailabelProductsAdminDtoByAdmin());
        model.addAttribute("paymentMethodList", OrderTable.PaymentMethod.values());
        model.addAttribute("shippingMethodList", OrderTable.ShippingMethod.values());
        model.addAttribute("orderStatusList", OrderTable.Status.values());
        model.addAttribute("users", userService.getAllAvailabelUsersNormalDtoByAdmin());
        model.addAttribute("coupons", couponService.getAllCouponValid());
        model.addAttribute("order", orderService.getOrderByOrderNumber(orderNumber));
        return "admin/order/detail";
    }
}
