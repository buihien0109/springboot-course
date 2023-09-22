package vn.techmaster.ecommecerapp.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vn.techmaster.ecommecerapp.model.projection.ProductPublic;
import vn.techmaster.ecommecerapp.security.SecurityUtils;
import vn.techmaster.ecommecerapp.service.CartService;
import vn.techmaster.ecommecerapp.service.CategoryService;
import vn.techmaster.ecommecerapp.service.ProductService;

@Controller
public class WebController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CartService cartService;

    public WebController(CategoryService categoryService, ProductService productService, CartService cartService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("data", productService.findAllProductCombineCategoryAndDiscount(1, 8));
        return "web/index";
    }

    @GetMapping("/danh-muc/{categorySlug}")
    public String getCategory(
            Model model, @PathVariable String categorySlug,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "9") int size,
            @RequestParam(required = false, defaultValue = "") String sub
    ) {
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSub", sub);
        model.addAttribute("categoryName", categoryService.findBySlug(categorySlug).getName());
        model.addAttribute("categorySlug", categorySlug);
        model.addAttribute("subCategories", categoryService.findAllByParentCategorySlug(categorySlug));
        model.addAttribute("pageData", productService.findAllProductByParentCategorySlug(categorySlug, page, size, sub));
        return "web/category";
    }

    @GetMapping("/san-pham/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        ProductPublic product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", productService.findAllProductByCategoryIdAndProductIdNot(product.getCategory().getCategoryId(), id, 4));
        return "web/product-detail";
    }

    @GetMapping("/gio-hang")
    public String getCart(Model model) {
        if(!SecurityUtils.isAuthenticated()) {
            return "redirect:/dang-nhap";
        }
        return "web/shopping-cart";
    }

    @GetMapping("/yeu-thich")
    public String getWishlist() {
        return "web/wishlist";
    }

    @GetMapping("/bai-viet")
    public String getPost() {
        return "web/post";
    }

    @GetMapping("/bai-viet/{id}")
    public String getPostDetail(@PathVariable Long id) {
        return "web/post-detail";
    }

    @GetMapping("/thanh-toan")
    public String getCheckout() {
        if(SecurityUtils.isAuthenticated()) {
            return "redirect:/";
        }
        return "web/checkout";
    }

    @GetMapping("/dang-nhap")
    public String getLogin() {
        if(SecurityUtils.isAuthenticated()) {
            return "redirect:/";
        }
        return "web/login";
    }

    @GetMapping("/dang-ky")
    public String getRegister() {
        if(SecurityUtils.isAuthenticated()) {
            return "redirect:/";
        }
        return "web/register";
    }

    @GetMapping("/quen-mat-khau")
    public String getForgotPassword() {
        if(SecurityUtils.isAuthenticated()) {
            return "redirect:/";
        }
        return "web/forgot-password";
    }
}
