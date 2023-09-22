package vn.techmaster.ecommecerapp.model.request;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemRequest {
    private Integer quantity;
}
