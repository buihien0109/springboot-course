package vn.techmaster.ecommecerapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.service.ProductService;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getProductPage(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/product/index";
    }

    @GetMapping("/create")
    public String getProductCreatePage(Model model) {
        return "admin/product/create";
    }

    @GetMapping("/{id}/detail")
    public String getProductDetailPage(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "admin/product/detail";
    }
}
