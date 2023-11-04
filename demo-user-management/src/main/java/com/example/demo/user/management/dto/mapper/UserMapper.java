package com.example.demo.user.management.dto.mapper;

import com.example.demo.user.management.dto.UserDto;
import com.example.demo.user.management.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getAvatar()
        );
    }
}
