package com.example.demo.thymeleaf.crud.service;

import com.example.demo.thymeleaf.crud.entity.Product;
import com.example.demo.thymeleaf.crud.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FileService fileService;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return productRepository.findAll(pageable);
    }

    public Page<Product> findPaginatedWithSearch(String keyword, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameContaining(keyword, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void save(Product product, MultipartFile file) throws IOException {
        // Lưu ảnh vào folder
        String filePath = fileService.saveFile(file);
        product.setImageUrl(filePath);
        productRepository.save(product);
    }

    public void update(Product product, MultipartFile file) throws IOException {
        log.info("Product: {}", product);

        Product existingProduct = productRepository.findById(product.getId()).orElse(null);
        if (existingProduct != null) {
            log.info("Existing Product: {}", existingProduct);

            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setPrice(product.getPrice());

            // Kiểm tra xem có file ảnh mới không
            if (file != null && !file.isEmpty()) {
                log.info("New Image");
                String filePath = fileService.saveFile(file);
                existingProduct.setImageUrl(filePath);
            }

            productRepository.save(product);
        }
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
