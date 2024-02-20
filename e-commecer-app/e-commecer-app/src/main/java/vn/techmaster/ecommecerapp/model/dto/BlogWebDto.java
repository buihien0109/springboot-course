package vn.techmaster.ecommecerapp.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogWebDto {
    Integer id;
    String title;
    String slug;
    String description;
    String thumbnail;
    Date publishedAt;
}