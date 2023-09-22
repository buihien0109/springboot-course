package vn.techmaster.ecommecerapp.rest.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.WishListRequest;
import vn.techmaster.ecommecerapp.service.WishListService;

@RestController
@RequestMapping("/api/v1/wishlist")
public class WishListResources {
    private final WishListService wishListService;

    public WishListResources(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @PostMapping()
    public ResponseEntity<?> addToWishList(@RequestBody WishListRequest wishListRequest) {
        wishListService.addProductToWishList(wishListRequest.getProductId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWishList(@PathVariable Long id) {
        wishListService.deleteProductFromWishList(id);
        return ResponseEntity.ok().build();
    }
}
