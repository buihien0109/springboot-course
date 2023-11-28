package com.example.demo.thymeleaf.crud.controller;

import com.example.demo.thymeleaf.crud.entity.User;
import com.example.demo.thymeleaf.crud.request.AddToCartRequest;
import com.example.demo.thymeleaf.crud.request.UpdateCartItemRequest;
import com.example.demo.thymeleaf.crud.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

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
    public ResponseEntity<?> updateCartItem(HttpSession session,
                                            @Valid @RequestBody UpdateCartItemRequest request) {
        cartService.updateCartItemQuantity(request);
        return ResponseEntity.ok("Số lượng sản phẩm đã được cập nhật");
    }

    @DeleteMapping("/api/cart/remove-item/{cartItemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseEntity.ok("Sản phẩm đã được xóa khỏi giỏ hàng");
    }
}
