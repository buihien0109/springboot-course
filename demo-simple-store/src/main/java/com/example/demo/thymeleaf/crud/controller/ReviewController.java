package com.example.demo.thymeleaf.crud.controller;

import com.example.demo.thymeleaf.crud.request.ReviewData;
import com.example.demo.thymeleaf.crud.service.ProductReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ProductReviewService productReviewService;

    @PostMapping("/products/{productId}/reviews/add")
    public String addReview(@PathVariable Long productId, @ModelAttribute @Valid ReviewData reviewData, BindingResult result, RedirectAttributes redirectAttributes) {
        log.info("Review data: {}", reviewData);
        if (result.hasErrors()) {
            log.error("Error: {}", result.getAllErrors());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.reviewData", result);
            log.info("org.springframework.validation.BindingResult.reviewData: {}", result);
            redirectAttributes.addFlashAttribute("reviewData", reviewData);
            return "redirect:/products/" + productId;
        }

        // Lưu review vào database
        productReviewService.addReview(productId, reviewData);

        return "redirect:/products/" + productId;
    }

    @GetMapping("/products/{productId}/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Long productId, @PathVariable Long reviewId) {
        productReviewService.deleteReview(reviewId);
        return "redirect:/products/" + productId;
    }
}
