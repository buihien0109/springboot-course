package com.example.demo.thymeleaf.crud.service;

import com.example.demo.thymeleaf.crud.entity.*;
import com.example.demo.thymeleaf.crud.repository.OrderItemRepository;
import com.example.demo.thymeleaf.crud.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;

    public void save(Order order, User currentUser) {
        // Lấy thông tin giỏ hàng
        Cart cart = cartService.getCart(currentUser);

        // Lưu thông tin đơn hàng
        order.setUser(currentUser);

        // Lưu thông tin đơn hàng
        cart.getItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            order.addItem(orderItem);
        });

        // Xóa giỏ hàng
        cartService.clearCart(currentUser);

        orderRepository.save(order);
    }
}
