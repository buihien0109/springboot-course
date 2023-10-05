package vn.techmaster.ecommecerapp.model.request;

import lombok.*;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateDiscountCampaingRequest {
    private String name;
    private String description;
    private DiscountCampaign.DiscountType discountType;
    private Integer discountValue;
    private Date startDate;
    private Date endDate;
    private DiscountCampaign.Status status;
    private List<Long> productIds;
}
