package vn.techmaster.ecommecerapp.rest.web;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.AddToCartRequest;
import vn.techmaster.ecommecerapp.service.CartService;

@RestController
@RequestMapping("/api/v1/cart")
public class CartResources {
    private final CartService cartService;

    public CartResources(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<?> getCart() {
        return ResponseEntity.ok(cartService.getCartForLoggedInUser());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(request));
    }
}
