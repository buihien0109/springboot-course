package vn.techmaster.ecommecerapp.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link vn.techmaster.ecommecerapp.entity.DiscountCampaign}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountCampaignNormalDto implements Serializable {
    Long campaignId;
    String name;
    DiscountCampaign.DiscountType discountType;
    Integer discountValue;
    Date startDate;
    Date endDate;
    Integer productCount;

    public DiscountCampaignNormalDto(Long campaignId, String name, String discountType, Integer discountValue, String startDate, String endDate, Integer productCount) {
        this.campaignId = campaignId;
        this.name = name;
        this.discountType = DiscountCampaign.DiscountType.valueOf(discountType);
        this.discountValue = discountValue;
        this.startDate = DateUtils.parseDate(startDate);
        this.endDate = DateUtils.parseDate(endDate);
        this.productCount = productCount;
    }
}