package com.example.demo.thymeleaf.crud.controller;

import com.example.demo.thymeleaf.crud.entity.Blog;
import com.example.demo.thymeleaf.crud.entity.Order;
import com.example.demo.thymeleaf.crud.entity.User;
import com.example.demo.thymeleaf.crud.request.AddToCartRequest;
import com.example.demo.thymeleaf.crud.request.UpdateCartItemRequest;
import com.example.demo.thymeleaf.crud.service.CartService;
import com.example.demo.thymeleaf.crud.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        log.info("User: {}", user);
        if (user == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("cart", cartService.getCart(user));
        }
        return "web/cart";
    }

    @GetMapping("/checkout")
    public String viewCheckout(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        log.info("User: {}", user);
        if (user == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("shippingMethods", Order.ShippingMethod.values());
            model.addAttribute("paymentMethods", Order.PaymentMethod.values());
            model.addAttribute("user", user);
            model.addAttribute("order", new Order());
            model.addAttribute("cart", cartService.getCart(user));
        }
        return "web/checkout";
    }

    @PostMapping("/saveOrder")
    public String checkout(@Valid @ModelAttribute("order") Order order, BindingResult result, HttpSession session, Model model) {
        log.info("Order: {}", order);

        // Xác định người dùng hiện tại
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            model.addAttribute("cart", cartService.getCart(user));
            model.addAttribute("shippingMethods", Order.ShippingMethod.values());
            model.addAttribute("paymentMethods", Order.PaymentMethod.values());
            model.addAttribute("user", user);
            return "web/checkout";
        }

        orderService.save(order, user);
        return "redirect:/";
    }

    @PostMapping("/api/cart/add")
    public ResponseEntity<?> addProductToCart(HttpSession session,
                                              @Valid @RequestBody AddToCartRequest request) {
        User user = (User) session.getAttribute("currentUser");
        log.info("User: {}", user);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
        } else {
            cartService.addProductToCart(user, request);
            return ResponseEntity.ok("Sản phẩm đã được thêm vào giỏ hàng");
        }
    }

    @PostMapping("/api/cart/update-item")
    public ResponseEntity<?> updateCartItem(@Valid @RequestBody UpdateCartItemRequest request) {
        cartService.updateCartItemQuantity(request);
        return ResponseEntity.ok("Số lượng sản phẩm đã được cập nhật");
    }

    @DeleteMapping("/api/cart/remove-item/{cartItemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseEntity.ok("Sản phẩm đã được xóa khỏi giỏ hàng");
    }
}
