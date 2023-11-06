package com.example.demo.course.db;

import com.example.demo.course.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDB {
    public static List<User> userList = new ArrayList<>(
            List.of(
                    new User(1, "Duc Thinh", "thinh@gmail.com", "0988888888", "https://media.techmaster.vn/api/static/c2m5ou451cob24f6skeg/c3IwVOU2"),
                    new User(2, "Pham Man", "man@gmail.com", "0988888887", "https://media.techmaster.vn/api/static/crop/bv9jp4k51co7nj2mhht0"),
                    new User(3, "Thanh Huong", "huong@gmail.com", "0988888886", "https://media.techmaster.vn/api/static/crop/brm3huc51co50mv77sag")
            )
    );
}
