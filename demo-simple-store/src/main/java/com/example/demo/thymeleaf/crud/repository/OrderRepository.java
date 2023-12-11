package com.example.demo.thymeleaf.crud.repository;

import com.example.demo.thymeleaf.crud.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}