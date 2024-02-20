package vn.techmaster.ecommecerapp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.security.SecurityUtils;
import vn.techmaster.ecommecerapp.service.CartService;
import vn.techmaster.ecommecerapp.service.CategoryService;
import vn.techmaster.ecommecerapp.service.WishListService;

import java.util.Objects;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class GlobalDataInit {
    private final CategoryService categoryService;
    private final CartService cartService;
    private final WishListService wishListService;

    // pass object golbal data from thymeleaf to html
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new HandlerInterceptor() {
                    @Override
                    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                        if (modelAndView != null && !Objects.requireNonNull(modelAndView.getViewName()).startsWith("admin")) {
                            User user = SecurityUtils.getCurrentUserLogin();
                            modelAndView.addObject("categories", categoryService.findAllByParentCategoryIsNull());
                            modelAndView.addObject("cart", user != null ? cartService.getCartForLoggedInUser() : cartService.getCartForGuestUser());
                            modelAndView.addObject("wishList", user != null ? wishListService.getAllWishListForLoggedInUser() : wishListService.getAllWishListForGuestUser());
                            modelAndView.addObject("currentUser", user);
                            modelAndView.addObject("isLogin", user != null);
                        }
                    }
                });
            }
        };
    }


}
