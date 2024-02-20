package vn.techmaster.ecommecerapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link vn.techmaster.ecommecerapp.entity.Tag}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TagUsedDto implements Serializable {
    private Integer id;
    private String name;
    private String slug;
    private Integer used;
}