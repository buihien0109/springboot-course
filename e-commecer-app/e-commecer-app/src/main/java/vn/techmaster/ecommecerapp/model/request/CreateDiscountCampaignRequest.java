package vn.techmaster.ecommecerapp.model.request;

import lombok.*;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateDiscountCampaignRequest {
    private String name;
    private String description;
    private DiscountCampaign.DiscountType discountType;
    private Integer discountValue;
    private Date startDate;
    private Date endDate;
}
