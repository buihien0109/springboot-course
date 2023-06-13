package com.example.demo;

import lombok.*;

// @Data = @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
// @FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    private Integer id;
    private String name;
    private String email;
}
