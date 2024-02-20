package vn.techmaster.ecommecerapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class DiscountCampaignNormalDto implements Serializable {
    private Long campaignId;
    private String name;
    private DiscountCampaign.DiscountType discountType;
    private Integer discountValue;
    private Date startDate;
    private Date endDate;
    private Integer productCount;

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