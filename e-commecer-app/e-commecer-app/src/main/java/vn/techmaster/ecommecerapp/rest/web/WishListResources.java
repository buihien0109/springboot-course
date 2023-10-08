package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.projection.WishListPublic;
import vn.techmaster.ecommecerapp.model.request.WishListRequest;
import vn.techmaster.ecommecerapp.service.WishListService;

@RestController
@RequestMapping("/api/v1/public/wishlist")
@RequiredArgsConstructor
public class WishListResources {
    private final WishListService wishListService;

    @PostMapping()
    public ResponseEntity<?> addToWishList(@RequestBody WishListRequest wishListRequest) {
        WishListPublic wishList = wishListService.addProductToWishList(wishListRequest.getProductId());
        return ResponseEntity.ok(wishList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWishList(@PathVariable Long id) {
        wishListService.deleteProductFromWishList(id);
        return ResponseEntity.ok().build();
    }
}
