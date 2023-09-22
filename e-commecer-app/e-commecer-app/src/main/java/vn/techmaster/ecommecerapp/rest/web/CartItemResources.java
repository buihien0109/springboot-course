package vn.techmaster.ecommecerapp.rest.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.UpdateCartItemRequest;
import vn.techmaster.ecommecerapp.service.CartItemService;

@RestController
@RequestMapping("/api/v1/cart-items")
public class CartItemResources {
    private final CartItemService cartItemService;

    public CartItemResources(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    // create method update cart item by id
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCartItemById(@RequestBody UpdateCartItemRequest request, @PathVariable Long id) {
        cartItemService.updateCartItemById(request, id);
        return ResponseEntity.ok().build();
    }

    // create method delete cart item by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCartItemById(@PathVariable Long id) {
        cartItemService.deleteCartItemById(id);
        return ResponseEntity.ok().build();
    }
}
