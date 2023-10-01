package vn.techmaster.ecommecerapp.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpsertSubCategory {
    private Long parentCategoryId;
    private String name;
}
