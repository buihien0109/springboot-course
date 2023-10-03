package vn.techmaster.ecommecerapp.model.request;

import lombok.*;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpsertDiscountCampaignRequest {
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private DiscountCampaign.Status status;
}
