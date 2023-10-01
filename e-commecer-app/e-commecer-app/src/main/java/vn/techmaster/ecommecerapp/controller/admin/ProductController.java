package vn.techmaster.ecommecerapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.service.CategoryService;
import vn.techmaster.ecommecerapp.service.ProductService;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

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
        return "admin/product/detail";
    }
}
