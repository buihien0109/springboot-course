package com.example.demo.user.management.model;

import lombok.*;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private String password;
}

