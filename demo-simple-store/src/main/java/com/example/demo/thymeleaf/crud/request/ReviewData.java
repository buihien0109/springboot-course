package com.example.demo.thymeleaf.crud.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewData {
    @NotEmpty(message = "Nội dung review không được để trống")
    @NotNull(message = "Nội dung review không được để trống")
    private String content;

    @NotNull(message = "Đánh giá không được để trống")
    @Min(value = 1, message = "Đánh giá phải lớn hơn 0")
    @Max(value = 5, message = "Đánh giá phải nhỏ hơn hoặc bằng 5")
    private Integer rating;
}
