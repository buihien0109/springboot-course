package vn.techmaster.ecommecerapp.service;

import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.entity.WishList;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.WishListPublic;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.WishListRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;

import java.util.List;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;

    public WishListService(WishListRepository wishListRepository, ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
    }

    // create method get list WishListPublic by userId
    public List<WishListPublic> getAllWishListByUserId() {
        User user = SecurityUtils.getCurrentUserLogin();
        List<WishList> wishLists = wishListRepository.findByUser_UserId(user.getUserId());
        return wishLists.stream().map(WishListPublic::of).toList();
    }

    // add product to wish list with param productId
    public WishListPublic addProductToWishList(Long productId) {
        User user = SecurityUtils.getCurrentUserLogin();

        // check product is exist in wish list
        if (wishListRepository.existsByUser_UserIdAndProduct_ProductId(user.getUserId(), productId)) {
            throw new ResouceNotFoundException("Product is exist in wish list");
        }

        // find product by productId and set to wishList
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResouceNotFoundException("Cannot find product by id " + productId));

        WishList wishList = new WishList();
        wishList.setUser(user);
        wishList.setProduct(product);
        wishListRepository.save(wishList);

        return WishListPublic.of(wishList);
    }

    // delete product from wish list with param wishListId
    public void deleteProductFromWishList(Long wishListId) {
        User user = SecurityUtils.getCurrentUserLogin();
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new ResouceNotFoundException("Cannot find wish list by id " + wishListId));
        if (wishList.getUser().getUserId().equals(user.getUserId())) {
            wishListRepository.deleteById(wishListId);
        } else {
            throw new ResouceNotFoundException("Cannot find wish list by id " + wishListId);
        }
    }
}
