package org.example.demo.dao;

import org.example.demo.db.ProductDB;
import org.example.demo.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductDAO {
    public List<Product> findAll() {
        return ProductDB.products;
    }

    public List<Product> findByTitleContainingIgnoreCase(String keyword) {
        return ProductDB.products.stream()
                .filter(product -> product.getName().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    public Optional<Product> findById(Integer id) {
        return ProductDB.products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }
}
