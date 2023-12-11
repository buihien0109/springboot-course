package com.example.demo.thymeleaf.crud.repository;

import com.example.demo.thymeleaf.crud.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}