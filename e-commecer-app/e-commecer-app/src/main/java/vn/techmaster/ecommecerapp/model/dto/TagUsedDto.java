package vn.techmaster.ecommecerapp.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * DTO for {@link vn.techmaster.ecommecerapp.entity.Tag}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagUsedDto implements Serializable {
    Integer id;
    String name;
    String slug;
    Integer used;
}