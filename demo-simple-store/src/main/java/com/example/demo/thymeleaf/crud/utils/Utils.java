package com.example.demo.thymeleaf.crud.utils;

import com.example.demo.thymeleaf.crud.entity.CartItem;

import java.util.List;

public class Utils {
    public static Integer calculateTotalMoney(List<CartItem> cartItems) {
        int totalMoney = 0;
        for (CartItem cartItem : cartItems) {
            totalMoney += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        return totalMoney;
    }

    // Tính tong 2 số a, b
    public static Integer sum(Integer a, Integer b) {
        return a + b;
    }
}
