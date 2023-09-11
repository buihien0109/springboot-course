package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.ProductDTO;
import com.example.demo.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> findAll() {
        final List<Product> products = productRepository.findAll(Sort.by("id"));
        return products.stream()
                .map(product -> mapToDTO(product, new ProductDTO()))
                .toList();
    }

    public ProductDTO get(final Integer id) {
        return productRepository.findById(id)
                .map(product -> mapToDTO(product, new ProductDTO()))
                .orElseThrow(() -> new NotFoundException("Not found product"));
    }

    public ProductDTO create(final ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        productRepository.save(product);
        return mapToDTO(product, new ProductDTO());
    }

    public ProductDTO update(final Integer id, final ProductDTO productDTO) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found product"));
        mapToEntity(productDTO, product);
        productRepository.save(product);
        return mapToDTO(product, new ProductDTO());
    }

    public void delete(final Integer id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found product"));
        productRepository.deleteById(id);
    }

    private ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        return productDTO;
    }

    private Product mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        return product;
    }

}
