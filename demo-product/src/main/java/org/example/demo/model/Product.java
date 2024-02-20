package org.example.demo.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    Integer id;
    String name;
    String description;
    String thumbnail;
    Integer price;
    Double rating;
    Integer priceDiscount;
}
