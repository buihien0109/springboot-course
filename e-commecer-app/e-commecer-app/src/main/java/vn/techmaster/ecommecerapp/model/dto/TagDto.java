package vn.techmaster.ecommecerapp.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagDto {
    Integer id;
    String name;
    String slug;
}
