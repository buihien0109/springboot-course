package com.example.demo.thymeleaf.crud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Tên không được để trống")
    private String name;

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotEmpty(message = "SDT không được để trống")
    private String phone;

    private String avatar;
    private String address;

    @NotEmpty(message = "Mật khẩu không được để trống")
    private String password;

    @NotEmpty(message = "Role không được để trống")
    private String role;
}
