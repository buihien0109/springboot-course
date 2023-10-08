package vn.techmaster.ecommecerapp.controller.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vn.techmaster.ecommecerapp.model.projection.ProductPublic;
import vn.techmaster.ecommecerapp.security.SecurityUtils;
import vn.techmaster.ecommecerapp.service.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final BlogService blogService;
    private final TagService tagService;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final BannerService bannerService;


    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("data", productService.findAllProductCombineCategoryAndDiscount(1, 8));
        model.addAttribute("banners", bannerService.getAllBannersPublic());
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
        model.addAttribute("reviews", reviewService.getAllReviewsByProductId(id));
        return "web/product-detail";
    }

    @GetMapping("/gio-hang")
    public String getCart() {
        return "web/shopping-cart";
    }

    @GetMapping("/yeu-thich")
    public String getWishlist() {
        return "web/wishlist";
    }

    @GetMapping("/bai-viet")
    public String getPost(
            Model model,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "6") Integer size,
            @RequestParam(required = false, defaultValue = "") String tag
    ) {
        log.info("page: {}, size: {}, tag: {}", page, size, tag);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentTag", tag);
        model.addAttribute("recentBlogs", blogService.getAllBlogs(3));
        model.addAttribute("pageData", blogService.getAllBlogs(tag, page, size));
        model.addAttribute("tags", tagService.getAllTags());
        return "web/post";
    }

    @GetMapping("/bai-viet/{id}/{slug}")
    public String getPostDetail(@PathVariable Integer id, @PathVariable String slug, Model model) {
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("recentBlogs", blogService.getAllBlogs(3));
        model.addAttribute("blog", blogService.getBlogByIdAndSlug(id, slug));
        model.addAttribute("relatedBlogs", blogService.getRelatedBlogs(id, slug));
        return "web/post-detail";
    }

    @GetMapping("/thanh-toan")
    public String getCheckout() {
        return "web/checkout";
    }

    @GetMapping("/dang-nhap")
    public String getLogin() {
        if (SecurityUtils.isAuthenticated()) {
            return "redirect:/";
        }
        return "web/login";
    }

    @GetMapping("/dang-ky")
    public String getRegister() {
        if (SecurityUtils.isAuthenticated()) {
            return "redirect:/";
        }
        return "web/register";
    }

    @GetMapping("/quen-mat-khau")
    public String getForgotPassword() {
        if (SecurityUtils.isAuthenticated()) {
            return "redirect:/";
        }
        return "web/forgot-password";
    }

    @GetMapping("/xac-nhan-don-hang/{orderNumber}")
    public String getOrderDetail(@PathVariable String orderNumber, Model model) {
        model.addAttribute("orderNumber", orderNumber);
        model.addAttribute("order", orderService.getOrderByOrderNumber(orderNumber));
        return "web/order-detail";
    }

    @GetMapping("/mail")
    public String getMail() {
        return "web/mail-template/confirmation-account";
    }
}
