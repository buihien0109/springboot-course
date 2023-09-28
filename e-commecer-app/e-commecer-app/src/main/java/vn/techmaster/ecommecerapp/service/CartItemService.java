package vn.techmaster.ecommecerapp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.CartItem;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.request.UpdateCartItemRequest;
import vn.techmaster.ecommecerapp.model.utils.CartItemInCookie;
import vn.techmaster.ecommecerapp.repository.CartItemRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;
import vn.techmaster.ecommecerapp.utils.CartUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final ProductRepository productRepository;

    // create method update cart item by cartItemId and quantity
    public void updateCartItemById(UpdateCartItemRequest request, Long cartItemId) {
        log.info("updateCartItemById");
        log.info("cartItemId: {}", cartItemId);

        User user = SecurityUtils.getCurrentUserLogin();
        if (user != null) {
            updateCartItemForLoggedInUser(request, cartItemId);
        } else {
            updateCartItemForGuestUser(request, cartItemId);
        }
    }

    public void updateCartItemForLoggedInUser(UpdateCartItemRequest request, Long cartItemId) {
        log.info("updateCartItemForLoggedInUser");

        // find cart item by cartItemId, if cart item not found, throw exception
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng"));

        // check if product quantity is enough
        if (cartItem.getProduct().getStockQuantity() < cartItem.getQuantity() + request.getQuantity()) {
            throw new BadRequestException("Số lượng sản phẩm không đủ");
        }

        // update cart item quantity
        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());

        // save cart item
        cartItemRepository.save(cartItem);
    }

    public void updateCartItemForGuestUser(UpdateCartItemRequest request, Long cartItemId) {
        log.info("updateCartItemForGuestUser");

        List<CartItemInCookie> cartItemInCookies = CartUtils.getCartFromCookie(httpServletRequest);

        // find cart item by cartItemId, if cart item not found, throw exception
        Optional<CartItemInCookie> cartItemInCookie = cartItemInCookies.stream()
                .filter(item -> item.getCartItemId().equals(cartItemId))
                .findFirst();
        if (cartItemInCookie.isEmpty()) {
            throw new ResouceNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng");
        }

        // get product from cart item
        CartItemInCookie cartItem = cartItemInCookie.get();

        // find product by id, if product not found, throw exception
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm"));

        // check if product quantity is enough
        if (product.getStockQuantity() < cartItem.getQuantity() + request.getQuantity()) {
            throw new BadRequestException("Số lượng sản phẩm không đủ");
        }

        // update cart item quantity
        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());

        // save cart item to cookie
        CartUtils.setCartToCookie(httpServletResponse, cartItemInCookies);
    }

    // create method delete cart item by id
    public void deleteCartItemById(Long cartItemId) {
        log.info("deleteCartItemById");
        log.info("cartItemId: {}", cartItemId);

        // get user from logged in
        User user = SecurityUtils.getCurrentUserLogin();
        if (user != null) {
            deleteCartItemForLoggedInUser(cartItemId);
        } else {
            deleteCartItemForGuestUser(cartItemId);
        }
    }

    public void deleteCartItemForLoggedInUser(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng"));

        cartItemRepository.delete(cartItem);
    }

    public void deleteCartItemForGuestUser(Long cartItemId) {
        log.info("deleteCartItemForGuestUser");

        // get cart from cookie
        List<CartItemInCookie> cartItemInCookies = CartUtils.getCartFromCookie(httpServletRequest);

        // find cart item by cartItemId, if cart item not found, throw exception
        Optional<CartItemInCookie> cartItemInCookie = cartItemInCookies.stream()
                .filter(item -> item.getCartItemId().equals(cartItemId))
                .findFirst();

        if (cartItemInCookie.isEmpty()) {
            throw new ResouceNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng");
        }

        // remove cart item from cart
        cartItemInCookies.remove(cartItemInCookie.get());

        // save cart to cookie
        CartUtils.setCartToCookie(httpServletResponse, cartItemInCookies);
    }
}
