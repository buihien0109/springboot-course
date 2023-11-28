package com.example.demo.thymeleaf.crud.repository;

import com.example.demo.thymeleaf.crud.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long id);
}