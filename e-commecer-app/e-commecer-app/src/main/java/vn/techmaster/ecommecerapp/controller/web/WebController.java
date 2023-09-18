package vn.techmaster.ecommecerapp.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {
    @GetMapping("/")
    public String getHome() {
        return "web/index";
    }

    @GetMapping("/shop")
    public String getShop() {
        return "web/shop";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable Long id) {
        return "web/product-detail";
    }

    @GetMapping("/shopping-cart")
    public String getCart() {
        return "web/shopping-cart";
    }

    @GetMapping("/post")
    public String getPost() {
        return "web/post";
    }

    @GetMapping("/post/{id}")
    public String getPostDetail(@PathVariable Long id) {
        return "web/post-detail";
    }

    @GetMapping("/checkout")
    public String getCheckout() {
        return "web/checkout";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "web/contact";
    }
}
