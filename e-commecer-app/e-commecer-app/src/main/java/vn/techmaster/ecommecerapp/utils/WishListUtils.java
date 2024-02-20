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
import vn.techmaster.ecommecerapp.model.utils.WishListInCookie;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WishListUtils {
    public static List<WishListInCookie> getWishListsFromCookie(HttpServletRequest request) {
        List<WishListInCookie> wishListInCookies = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            String cookieValue = CookieUtils.getCookieValue(request, ConstantValue.WISHLIST_COOKIE_NAME);
            if (cookieValue != null) {
                wishListInCookies = objectMapper.readValue(cookieValue, new TypeReference<List<WishListInCookie>>() {
                });
            }
            return wishListInCookies;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return wishListInCookies;
    }

    public static void setWishListsToCookie(HttpServletResponse response, List<WishListInCookie> wishLists) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String originalWishList = objectMapper.writeValueAsString(wishLists);
            Cookie cookie = new Cookie(ConstantValue.WISHLIST_COOKIE_NAME, objectMapper.writeValueAsString(URLEncoder.encode(originalWishList, StandardCharsets.UTF_8)));
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
