package com.example.demo.thymeleaf.crud.controller;

import com.example.demo.thymeleaf.crud.entity.Product;
import com.example.demo.thymeleaf.crud.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public String viewHomePage(Model model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(required = false) String keyword) {
        Page<Product> productPage = productService.findPaginatedWithSearch(keyword, page, size);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("listProducts", productPage.getContent());
        model.addAttribute("keyword", keyword);
        return "product/index";
    }

    @GetMapping("/showNewProductForm")
    public String showNewProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "product/create";
    }

//    @PostMapping("/saveProduct")
//    public String saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult result) {
//        if (result.hasErrors()) {
//            return "product/create";
//        }
//
//        productService.save(product);
//        return "redirect:/admin/products";
//    }

    @PostMapping("/saveProduct")
    public String saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult result, @RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "product/create";
        }

        try {
            productService.save(product, file);
        } catch (IOException e) {
            log.error(e.getMessage());
            redirectAttributes.addFlashAttribute("message", "Upload failed!");
            return "product/create";
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        return "product/detail";
    }

//    @PostMapping("/updateProduct")
//    public String updateProduct(@Valid @ModelAttribute("product") Product product, BindingResult result) {
//        if (result.hasErrors()) {
//            return "product/detail";
//        }
//        productService.save(product);
//        return "redirect:/admin/products";
//    }

    @PostMapping("/updateProduct")
    public String updateProduct(@Valid @ModelAttribute("product") Product product, BindingResult result, @RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "product/detail";
        }

        try {
            productService.update(product, file);
        } catch (IOException e) {
            log.error(e.getMessage());
            redirectAttributes.addFlashAttribute("message", "Upload failed!");
            return "product/detail";
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

}
