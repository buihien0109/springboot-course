package com.example.demomockmvctest.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post {
    private Integer id;

    @NotNull(message = "Tiêu đề không được để trống")
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 5, max = 50, message = "Tiêu đề phải từ 5 đến 50 ký tự")
    private String title;

    @NotNull(message = "Tác giả không được để trống")
    @NotBlank(message = "Tác giả không được để trống")
    private String author;
}
