package org.example.demo.service;

import org.example.demo.dao.ProductDAO;
import org.example.demo.model.Product;
import org.example.demo.response.PageResponse;
import org.example.demo.response.PageResponseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDAO productDAO;

    public PageResponse<Product> getAllProducts(String keyword, int page, int size) {
        List<Product> products;
        if (keyword != null && !keyword.isEmpty()) {
            products = productDAO.findByTitleContainingIgnoreCase(keyword);
        } else {
            products = productDAO.findAll();
        }
        return new PageResponseImpl<>(products, page, size);
    }

    public Product getProductById(Integer id) {
        return productDAO.findById(id).orElse(null);
    }
}
