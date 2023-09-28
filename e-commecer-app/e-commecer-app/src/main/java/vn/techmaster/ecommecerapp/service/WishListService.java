package vn.techmaster.ecommecerapp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.entity.WishList;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.WishListPublic;
import vn.techmaster.ecommecerapp.model.utils.WishListInCookie;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.WishListRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;
import vn.techmaster.ecommecerapp.utils.WishListUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishListService {
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    // create method get list WishListPublic by userId
    public List<WishListPublic> getAllWishListForLoggedInUser() {
        User user = SecurityUtils.getCurrentUserLogin();
        List<WishList> wishLists = wishListRepository.findByUser_UserId(user.getUserId());
        return wishLists.stream().map(WishListPublic::of).toList();
    }

    public List<WishListPublic> getAllWishListForGuestUser() {
        log.info("getAllWishListForGuestUser");

        List<WishListInCookie> wishListInCookies = WishListUtils.getWishListsFromCookie(httpServletRequest);
        log.info("wishListInCookies: {}", wishListInCookies);

        return wishListInCookies.stream()
                .map(wishListItem -> {
                    Product product = productRepository.findById(wishListItem.getProductId())
                            .orElseThrow(() -> new ResouceNotFoundException("Cannot find product by id " + wishListItem.getProductId()));

                    // create wishlist from product
                    WishList wishList = new WishList();
                    wishList.setWishlistId(wishListItem.getWishlistId());
                    wishList.setProduct(product);
                    return WishListPublic.of(wishList);
                })
                .toList();
    }

    // add product to wish list with param productId
    public WishListPublic addProductToWishList(Long productId) {
        log.info("addProductToWishList");
        log.info("productId: {}", productId);

        User user = SecurityUtils.getCurrentUserLogin();
        if (user != null) {
            return addProductToWishListForLoggedInUser(user, productId);
        } else {
            return addProductToWishListForGuestUser(productId);
        }
    }

    public WishListPublic addProductToWishListForLoggedInUser(User user, Long productId) {
        log.info("addProductToWishListForLoggedInUser");

        // check product is exist in wish list
        if (wishListRepository.existsByUser_UserIdAndProduct_ProductId(user.getUserId(), productId)) {
            throw new ResouceNotFoundException("Sản phẩm đã tồn tại trong danh sách yêu thích");
        }

        // find product by productId and set to wishList
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + productId));

        WishList wishList = new WishList();
        wishList.setUser(user);
        wishList.setProduct(product);
        wishListRepository.save(wishList);

        return WishListPublic.of(wishList);
    }

    public WishListPublic addProductToWishListForGuestUser(Long productId) {
        log.info("addProductToWishListForGuestUser");

        List<WishListInCookie> wishListInCookies = WishListUtils.getWishListsFromCookie(httpServletRequest);
        log.info("wishListInCookies: {}", wishListInCookies);

        // check product is exist in wish list
        if (wishListInCookies.stream().anyMatch(wishListItem -> wishListItem.getProductId().equals(productId))) {
            throw new ResouceNotFoundException("Sản phẩm đã tồn tại trong danh sách yêu thích");
        }

        // Create WishListInCookie and add product to wish list
        WishListInCookie wishListInCookie = new WishListInCookie();
        wishListInCookie.setWishlistId(System.currentTimeMillis());
        wishListInCookie.setProductId(productId);
        wishListInCookies.add(wishListInCookie);

        // save wish list to cookie
        WishListUtils.setWishListsToCookie(httpServletResponse, wishListInCookies);

        // find product by productId and set to wishList
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id " + productId));

        WishList wishList = new WishList();
        wishList.setWishlistId(wishListInCookie.getWishlistId());
        wishList.setProduct(product);

        return WishListPublic.of(wishList);
    }

    // delete product from wish list with param wishListId
    public void deleteProductFromWishList(Long wishListId) {
        log.info("deleteProductFromWishList");
        log.info("wishListId: {}", wishListId);

        User user = SecurityUtils.getCurrentUserLogin();
        if (user != null) {
            deleteProductFromWishListForLoggedInUser(user, wishListId);
        } else {
            deleteProductFromWishListForGuestUser(wishListId);
        }
    }

    public void deleteProductFromWishListForLoggedInUser(User user, Long wishListId) {
        log.info("deleteProductFromWishListForLoggedInUser");

        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new ResouceNotFoundException("Cannot find wish list by id " + wishListId));
        if (wishList.getUser().getUserId().equals(user.getUserId())) {
            wishListRepository.deleteById(wishListId);
        } else {
            throw new ResouceNotFoundException("Cannot find wish list by id " + wishListId);
        }
    }

    public void deleteProductFromWishListForGuestUser(Long wishListId) {
        log.info("deleteProductFromWishListForGuestUser");

        List<WishListInCookie> wishListInCookies = WishListUtils.getWishListsFromCookie(httpServletRequest);
        log.info("wishListInCookies: {}", wishListInCookies);

        // check wish list is exist
        if (wishListInCookies.stream().noneMatch(wishListItem -> wishListItem.getWishlistId().equals(wishListId))) {
            throw new ResouceNotFoundException("Không tìm thấy sản phẩm trong danh sách yêu thích");
        }

        // delete wish list from cookie
        wishListInCookies.removeIf(wishListItem -> wishListItem.getWishlistId().equals(wishListId));
        WishListUtils.setWishListsToCookie(httpServletResponse, wishListInCookies);
    }
}
