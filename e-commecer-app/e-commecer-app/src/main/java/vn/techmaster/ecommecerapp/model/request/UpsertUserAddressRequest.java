package vn.techmaster.ecommecerapp.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpsertUserAddressRequest {
    private String province;
    private String district;
    private String ward;
    private String detail;
    private Boolean isDefault;
}
