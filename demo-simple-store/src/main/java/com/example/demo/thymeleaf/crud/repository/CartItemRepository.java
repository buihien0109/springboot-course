package com.example.demo.thymeleaf.crud.repository;

import com.example.demo.thymeleaf.crud.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}