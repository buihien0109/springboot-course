package org.example.demo.controller;

import org.example.demo.model.Product;
import org.example.demo.response.PageResponse;
import org.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String viewHomePage(Model model,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "8") Integer size,
                               @RequestParam(required = false) String keyword) {
        PageResponse<Product> pageData = productService.getAllProducts(keyword, page, size);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageData.getTotalPages());
        model.addAttribute("totalItems", pageData.getTotalElements());
        model.addAttribute("products", pageData.getContent());
        return "index";
    }

    @GetMapping("/products/{id}")
    public String viewProductDetail(Model model, @PathVariable Integer id) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product-detail";
    }
}
