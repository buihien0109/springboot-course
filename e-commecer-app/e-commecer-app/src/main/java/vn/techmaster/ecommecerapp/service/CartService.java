package vn.techmaster.ecommecerapp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Cart;
import vn.techmaster.ecommecerapp.entity.CartItem;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.CartPublic;
import vn.techmaster.ecommecerapp.model.request.AddToCartRequest;
import vn.techmaster.ecommecerapp.model.utils.CartItemInCookie;
import vn.techmaster.ecommecerapp.repository.CartRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;
import vn.techmaster.ecommecerapp.utils.CartUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    // create method get cart by user id
    // user info get from SecurityUtils
    public CartPublic getCartForLoggedInUser() {
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            return null;
        }
        Cart cart = cartRepository.findByUser_UserId(user.getUserId())
                .orElse(null);
        return CartPublic.of(cart);
    }

    // get cart by anonymous user
    public CartPublic getCartForGuestUser() {
        List<CartItemInCookie> cartItemInCookies = CartUtils.getCartFromCookie(httpServletRequest);
        return getCartItemInCookieToCartPublic(cartItemInCookies);
    }

    public Cart getCartForLoggedInUser(Long userId) {
        return cartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy giỏ hàng"));
    }

    public CartPublic addToCart(AddToCartRequest request) {
        // find product by id, if product not found, throw exception
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm"));

        // check if product quantity is enough
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new ResouceNotFoundException("Số lượng sản phẩm không đủ");
        }

        // get user from SecurityContextHolder
        User user = SecurityUtils.getCurrentUserLogin();
        if (user != null) {
            return addToCartForLoggedInUser(request, user, product);
        } else {
            return addToCartForGuestUser(request, product);
        }
    }

    private CartPublic addToCartForGuestUser(AddToCartRequest request, Product product) {
        List<CartItemInCookie> cartItemInCookies = CartUtils.getCartFromCookie(httpServletRequest);
        log.info("cartItemInCookies Before Add: {}", cartItemInCookies);

        // check if product is already in cart
        // if product is already in cart, increase quantity
        // if product is not in cart, add product to cart
        Optional<CartItemInCookie> cartItemInCookieOptional = cartItemInCookies.stream()
                .filter(cartItemInCookie -> cartItemInCookie.getProductId().equals(request.getProductId())).findFirst();
        if (cartItemInCookieOptional.isPresent()) {
            log.info("Cart item is already in cart");
            CartItemInCookie cartItemInCookie = cartItemInCookieOptional.get();

            // check if product quantity is enough
            if (product.getStockQuantity() < cartItemInCookie.getQuantity() + request.getQuantity()) {
                throw new BadRequestException("Số lượng sản phẩm không đủ");
            }

            cartItemInCookie.setQuantity(cartItemInCookie.getQuantity() + request.getQuantity());
        } else {
            log.info("Cart item is not in cart");
            // check if product quantity is enough
            if (product.getStockQuantity() < request.getQuantity()) {
                throw new BadRequestException("Số lượng sản phẩm không đủ");
            }

            CartItemInCookie cartItemInCookie = new CartItemInCookie();
            cartItemInCookie.setCartItemId(System.currentTimeMillis());
            cartItemInCookie.setProductId(request.getProductId());
            cartItemInCookie.setQuantity(request.getQuantity());
            cartItemInCookies.add(cartItemInCookie);
        }

        // save cart to cookie
        CartUtils.setCartToCookie(httpServletResponse, cartItemInCookies);

        return getCartItemInCookieToCartPublic(cartItemInCookies);
    }

    private CartPublic getCartItemInCookieToCartPublic(List<CartItemInCookie> cartItemInCookies) {
        Cart cart = new Cart();
        cartItemInCookies.forEach(cartItemInCookie -> {
            Product product = productRepository.findById(cartItemInCookie.getProductId())
                    .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm"));
            CartItem cartItem = new CartItem();
            cartItem.setCartItemId(cartItemInCookie.getCartItemId());
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemInCookie.getQuantity());
            cart.addCartItem(cartItem);
        });

        return CartPublic.of(cart);
    }

    private CartPublic addToCartForLoggedInUser(AddToCartRequest request, User user, Product product) {
        // check user has cart or not
        // if user has cart, add product to cart
        // if user has not cart, create new cart and add product to cart
        Cart cart = cartRepository.findByUser_UserId(user.getUserId()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        // check if product is already in cart
        // if product is already in cart, increase quantity
        // if product is not in cart, add product to cart
        cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getProductId().equals(request.getProductId())).findFirst().ifPresentOrElse(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity()), () -> {

                    CartItem cartItem = new CartItem();
                    cartItem.setProduct(product);
                    cartItem.setQuantity(request.getQuantity());
                    cart.addCartItem(cartItem);
                });

        // TODO: update product stock quantity
        // product.setStockQuantity(product.getStockQuantity() - request.getQuantity());
        // productRepository.save(product);

        // update cart
        cartRepository.save(cart);
        return CartPublic.of(cart);
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    public void syncCart() {
        // get cart from cookie
        List<CartItemInCookie> cartItemInCookies = CartUtils.getCartFromCookie(httpServletRequest);
        log.info("cartItemInCookies: {}", cartItemInCookies);

        // get user from SecurityContextHolder
        User user = SecurityUtils.getCurrentUserLogin();

        if (user != null) {
            // check user has cart or not
            // if user has cart, add product to cart
            // if user has not cart, create new cart and add product to cart
            Cart cart = cartRepository.findByUser_UserId(user.getUserId()).orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setUser(user);
                return cartRepository.save(newCart);
            });

            // check if product is already in cart. if has, increase quantity. if not, add product to cart
            cartItemInCookies.forEach(cartItemInCookie -> {
                cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getProduct().getProductId().equals(cartItemInCookie.getProductId())).findFirst().ifPresentOrElse(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + cartItemInCookie.getQuantity()), () -> {
                            Product product = productRepository.findById(cartItemInCookie.getProductId())
                                    .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm"));
                            CartItem cartItem = new CartItem();
                            cartItem.setProduct(product);
                            cartItem.setQuantity(cartItemInCookie.getQuantity());
                            cart.addCartItem(cartItem);
                        });
            });

            // update cart
            cartRepository.save(cart);

            // delete cart in cookie
            CartUtils.setCartToCookie(httpServletResponse, new ArrayList<>());
        }

    }
}
