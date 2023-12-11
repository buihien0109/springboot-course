package com.example.demo.thymeleaf.crud.controller;

import com.example.demo.thymeleaf.crud.entity.Blog;
import com.example.demo.thymeleaf.crud.entity.Product;
import com.example.demo.thymeleaf.crud.entity.ProductReview;
import com.example.demo.thymeleaf.crud.request.ReviewData;
import com.example.demo.thymeleaf.crud.service.BlogService;
import com.example.demo.thymeleaf.crud.service.ProductReviewService;
import com.example.demo.thymeleaf.crud.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final ProductService productService;
    private final BlogService blogService;
    private final ProductReviewService reviewService;

    @GetMapping("/")
    public String listProducts(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "web/index"; // Tên của template Thymeleaf
    }

    @GetMapping("/products/{id}")
    public String productDetail(Model model, @PathVariable Long id, @ModelAttribute("reviewData") ReviewData reviewData, BindingResult result) {
        if (reviewData == null) {
            reviewData = new ReviewData();
        }

        Product product = productService.findById(id).orElse(null);
        List<ProductReview> reviews = reviewService.getReviewsByProductId(id);
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewData", reviewData);
        return "web/productDetail";
    }

    @GetMapping("/blogs")
    public String listBlogs(Model model) {
        List<Blog> blogs = blogService.findAllBlogsPublished();
        model.addAttribute("blogs", blogs);
        return "web/blogList";
    }

    @GetMapping("/blogs/{id}")
    public String blogDetail(Model model, @PathVariable Long id) {
        Blog blog = blogService.findById(id).orElse(null);
        model.addAttribute("blog", blog);
        return "web/blogDetail";
    }
}
