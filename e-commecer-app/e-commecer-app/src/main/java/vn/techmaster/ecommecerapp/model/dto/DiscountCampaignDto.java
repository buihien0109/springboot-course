package vn.techmaster.ecommecerapp.model.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.utils.DateUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link vn.techmaster.ecommecerapp.entity.DiscountCampaign}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiscountCampaignDto implements Serializable {
    private Long campaignId;
    private String name;
    private String slug;
    private String description;
    private DiscountCampaign.DiscountType discountType;
    private Integer discountValue;
    private Date startDate;
    private Date endDate;
    private List<ProductNormalAdminDto> products;

    public DiscountCampaignDto(Long campaignId, String name, String slug, String description, String discountType, Integer discountValue, String startDate, String endDate, String products) {
        this.campaignId = campaignId;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.discountType = DiscountCampaign.DiscountType.valueOf(discountType);
        this.discountValue = discountValue;
        this.startDate = DateUtils.parseDate(startDate);
        this.endDate = DateUtils.parseDate(endDate);

        if (products != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.products = objectMapper.readValue(products, new TypeReference<List<ProductNormalAdminDto>>() {
                });
            } catch (IOException e) {
                this.products = new ArrayList<>();
            }
        } else {
            this.products = new ArrayList<>();
        }
    }
}