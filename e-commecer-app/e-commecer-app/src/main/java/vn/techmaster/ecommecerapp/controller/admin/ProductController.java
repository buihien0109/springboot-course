package vn.techmaster.ecommecerapp.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.service.CategoryService;
import vn.techmaster.ecommecerapp.service.ProductService;
import vn.techmaster.ecommecerapp.service.ReviewService;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    @GetMapping
    public String getProductPage(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/product/index";
    }

    @GetMapping("/create")
    public String getProductCreatePage(Model model) {
        model.addAttribute("statusList", Product.Status.values());
        model.addAttribute("categoryList", categoryService.findAllByParentCategoryIsNull());
        return "admin/product/create";
    }

    @GetMapping("/{id}/detail")
    public String getProductDetailPage(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("statusList", Product.Status.values());
        model.addAttribute("categoryList", categoryService.findAllByParentCategoryIsNull());
        model.addAttribute("reviewList", reviewService.getAllReviewsByProductIdByAdmin(id));

        return "admin/product/detail";
    }
}
