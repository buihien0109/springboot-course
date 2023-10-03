package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;

public interface DiscountCampaignRepository extends JpaRepository<DiscountCampaign, Long> {
}