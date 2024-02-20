package vn.techmaster.ecommecerapp.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    Long categoryId;
    String name;
    String slug;
    Long parentCategoryId;
    String parentCategoryName;
    String parentCategorySlug;
}
