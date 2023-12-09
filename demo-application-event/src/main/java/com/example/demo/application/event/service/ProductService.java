package com.example.demo.application.event.service;

import com.example.demo.application.event.entity.Product;
import com.example.demo.application.event.event.publisher.ProductEventPublisher;
import com.example.demo.application.event.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductEventPublisher eventPublisher;

    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm có id = " + id));
    }

    public Product saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        // Phát hành sự kiện tạo sản phẩm
        eventPublisher.publishProductEvent(savedProduct, "CREATE");
        return savedProduct;
    }

    public void deleteProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new RuntimeException("Không tìm thấy sản phẩm có id = " + id);
        }
        productRepository.deleteById(id);
        // Phát hành sự kiện xóa sản phẩm
        eventPublisher.publishProductEvent(productOptional.get(), "DELETE");
    }
}
