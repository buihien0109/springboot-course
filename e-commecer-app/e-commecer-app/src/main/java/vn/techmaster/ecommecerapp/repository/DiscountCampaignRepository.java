package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.model.dto.DiscountCampaignDto;
import vn.techmaster.ecommecerapp.model.dto.DiscountCampaignNormalDto;

import java.util.List;
import java.util.Optional;

public interface DiscountCampaignRepository extends JpaRepository<DiscountCampaign, Long> {
    @Query(nativeQuery = true, name = "getAllDiscountCampaignsNormalDto")
    List<DiscountCampaignNormalDto> getAllDiscountCampaignsNormalDto();

    @Query(nativeQuery = true, name = "getDiscountCampaignDtoById")
    Optional<DiscountCampaignDto> getDiscountCampaignDtoById(Long id);

    @Modifying
    @Query(value = "DELETE FROM product_discount WHERE discount_id = :campaignId", nativeQuery = true)
    void deleteProductsByCampaignId(@Param("campaignId") Long campaignId);
}