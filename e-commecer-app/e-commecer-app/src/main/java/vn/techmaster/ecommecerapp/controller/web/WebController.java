package vn.techmaster.ecommecerapp.controller.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vn.techmaster.ecommecerapp.entity.OrderTable;
import vn.techmaster.ecommecerapp.model.projection.product.ProductPublic;
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
    private final AuthService authService;
    private final UserAddressService userAddressService;


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

    @GetMapping("/san-pham/{id}/{slug}")
    public String getProduct(Model model, @PathVariable Long id, @PathVariable String slug) {
        ProductPublic product = productService.findByIdAndSlug(id, slug);
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", productService.getRelatedProducts(product.getCategory().getCategoryId(), id, 4)); // done
        model.addAttribute("reviews", reviewService.getAllReviewsAvailableByProductId(id)); // done
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
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String search
    ) {
        log.info("page: {}, size: {}, tag: {}", page, size, tag);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentTag", tag);
        model.addAttribute("recentBlogs", blogService.getLatestBlogs(3)); // done
        model.addAttribute("pageData", blogService.searchBlog(tag, search, page, size)); // done
        model.addAttribute("tags", tagService.getAllTags()); // done
        return "web/post";
    }

    @GetMapping("/bai-viet/{id}/{slug}")
    public String getPostDetail(@PathVariable Integer id, @PathVariable String slug, Model model) {
        model.addAttribute("tags", tagService.getAllTags()); // done
        model.addAttribute("recentBlogs", blogService.getRecommendBlogs(id, 3)); // done
        model.addAttribute("blog", blogService.getBlogByIdAndSlug(id, slug)); // done
        model.addAttribute("relatedBlogs", blogService.getRelatedBlogs(id, slug));
        return "web/post-detail";
    }

    @GetMapping("/thanh-toan")
    public String getCheckout(Model model) {
        if (SecurityUtils.isAuthenticated()) {
            model.addAttribute("addressList", userAddressService.findAllAddressByUserLogin());
        }
        model.addAttribute("paymentMethodList", OrderTable.PaymentMethod.values());
        model.addAttribute("shippingMethodList", OrderTable.ShippingMethod.values());
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

    @GetMapping("/xac-thuc-tai-khoan")
    public String confirm(@RequestParam String token, Model model) {
        model.addAttribute("data", authService.confirmEmail(token));
        return "web/xac-thuc-tai-khoan";
    }

    @GetMapping("/dat-lai-mat-khau")
    public String resetPassword(@RequestParam String token, Model model) {
        model.addAttribute("data", authService.confirmResetPassword(token));
        return "web/dat-lai-mat-khau";
    }
}
