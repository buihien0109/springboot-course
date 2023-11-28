package com.example.demo.thymeleaf.crud.service;

import com.example.demo.thymeleaf.crud.entity.Cart;
import com.example.demo.thymeleaf.crud.entity.CartItem;
import com.example.demo.thymeleaf.crud.entity.Product;
import com.example.demo.thymeleaf.crud.entity.User;
import com.example.demo.thymeleaf.crud.repository.CartItemRepository;
import com.example.demo.thymeleaf.crud.repository.CartRepository;
import com.example.demo.thymeleaf.crud.repository.ProductRepository;
import com.example.demo.thymeleaf.crud.repository.UserRepository;
import com.example.demo.thymeleaf.crud.request.AddToCartRequest;
import com.example.demo.thymeleaf.crud.request.UpdateCartItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addProductToCart(User user, AddToCartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElse(new Cart());

        // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cart.addItem(newItem);
            cartItemRepository.save(newItem);
        }

        if (cart.getId() == null) {
            cart.setUser(userRepository.findById(user.getId()).orElse(null));
            cartRepository.save(cart);
        }
    }

    public void updateCartItemQuantity(UpdateCartItemRequest request) {
        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại trong giỏ hàng"));

        // check if quantity is valid
        if (cartItem.getQuantity() + request.getQuantity() < 1) {
            throw new RuntimeException("Số lượng sản phẩm không hợp lệ");
        }
        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        cartItemRepository.save(cartItem);
    }

    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public Cart getCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElse(new Cart());
    }
}
