package vn.techmaster.ecommecerapp.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpsertBannerRequest {
    private String name;
    private String linkRedirect;
    private String url;
    private Boolean status;
}
