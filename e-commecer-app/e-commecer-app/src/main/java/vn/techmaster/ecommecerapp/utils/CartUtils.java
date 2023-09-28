package vn.techmaster.ecommecerapp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import vn.techmaster.ecommecerapp.constant.ConstantValue;
import vn.techmaster.ecommecerapp.model.utils.CartItemInCookie;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CartUtils {
    public static List<CartItemInCookie> getCartFromCookie(HttpServletRequest request) {
        List<CartItemInCookie> cartItemInCookies = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            String cookieValue = CookieUtils.getCookieValue(request, ConstantValue.CART_COOKIE_NAME);
            log.info("Cart cookie: {}", cookieValue);
            if (cookieValue != null) {
                cartItemInCookies = objectMapper.readValue(cookieValue, new TypeReference<List<CartItemInCookie>>() {});
            }
            return cartItemInCookies;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return cartItemInCookies;
    }

    public static void setCartToCookie(HttpServletResponse response, List<CartItemInCookie> cartItems) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String originalCart = objectMapper.writeValueAsString(cartItems);
            log.info("Cart: {}", originalCart);
            log.info("Cart Items Size: {}", cartItems.size());
            Cookie cookie = new Cookie(ConstantValue.CART_COOKIE_NAME, objectMapper.writeValueAsString(URLEncoder.encode(originalCart, StandardCharsets.UTF_8)));
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
