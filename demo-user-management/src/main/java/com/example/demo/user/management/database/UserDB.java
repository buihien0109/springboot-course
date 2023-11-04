package com.example.demo.user.management.database;

import com.example.demo.user.management.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDB {
    public static List<User> userList = new ArrayList<>(List.of(
            new User(1, "Bùi Hiên", "hien@gmail.com", "0344005816", "Tỉnh Thái Bình", null, "111"),
            new User(2, "Nguyễn Thu Hằng", "hang@gmail.com", "0123456789", "Tỉnh Nam Định", null, "111"),
            new User(3, "Bùi Phương Loan", "loan@gmail.com", "0123456789", "Tỉnh Hưng Yên", null, "111")
    ));
}
