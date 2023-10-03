package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface DiscountCampaignPublic {
    Long getCampaignId();

    String getName();

    String getSlug();

    String getDescription();

    Date getStartDate();

    Date getEndDate();

    DiscountCampaign.Status getStatus();

    List<DiscountPublic> getDiscounts();

    @RequiredArgsConstructor
    class DiscountCampaignPublicImpl implements DiscountCampaignPublic {
        @JsonIgnore
        private final DiscountCampaign discountCampaign;

        @Override
        public Long getCampaignId() {
            return discountCampaign.getCampaignId();
        }

        @Override
        public String getName() {
            return discountCampaign.getName();
        }

        @Override
        public String getSlug() {
            return discountCampaign.getSlug();
        }

        @Override
        public String getDescription() {
            return discountCampaign.getDescription();
        }

        @Override
        public Date getStartDate() {
            return discountCampaign.getStartDate();
        }

        @Override
        public Date getEndDate() {
            return discountCampaign.getEndDate();
        }

        @Override
        public DiscountCampaign.Status getStatus() {
            return discountCampaign.getStatus();
        }

        @Override
        public List<DiscountPublic> getDiscounts() {
            if (discountCampaign.getDiscounts() == null) {
                return new ArrayList<>();
            }
            return discountCampaign.getDiscounts().stream().map(DiscountPublic::of).toList();
        }
    }

    static DiscountCampaignPublic of(DiscountCampaign discountCampaign) {
        return new DiscountCampaignPublicImpl(discountCampaign);
    }
}
