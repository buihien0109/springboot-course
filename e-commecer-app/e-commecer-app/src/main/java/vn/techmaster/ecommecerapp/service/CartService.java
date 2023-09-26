package vn.techmaster.ecommecerapp.service;

import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Cart;
import vn.techmaster.ecommecerapp.entity.CartItem;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.CartItemPublic;
import vn.techmaster.ecommecerapp.model.projection.CartPublic;
import vn.techmaster.ecommecerapp.model.request.AddToCartRequest;
import vn.techmaster.ecommecerapp.repository.CartRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    // create method get cart by user id
    // user info get from SecurityUtils
    public CartPublic getCartByUserId() {
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            return null;
        }
        Cart cart = cartRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new ResouceNotFoundException("Cart not found"));
        return CartPublic.of(cart);
    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResouceNotFoundException("Cart not found"));
    }

    public CartPublic addToCart(AddToCartRequest request) {
        // find product by id, if product not found, throw exception
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResouceNotFoundException("Product not found"));

        // check if product quantity is enough
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new ResouceNotFoundException("Product quantity is not enough");
        }

        // get user from SecurityContextHolder
        User user = SecurityUtils.getCurrentUserLogin();

        // check user has cart or not
        // if user has cart, add product to cart
        // if user has not cart, create new cart and add product to cart
        Cart cart = cartRepository.findByUser_UserId(user.getUserId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // check if product is already in cart
        // if product is already in cart, increase quantity
        // if product is not in cart, add product to cart
        cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getProductId().equals(request.getProductId()))
                .findFirst()
                .ifPresentOrElse(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity()), () -> {
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
}
