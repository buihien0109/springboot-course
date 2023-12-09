package com.example.demo.application.event.repository;

import com.example.demo.application.event.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}