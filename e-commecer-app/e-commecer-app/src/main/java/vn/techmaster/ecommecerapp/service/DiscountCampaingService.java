package vn.techmaster.ecommecerapp.service;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.dto.DiscountCampaignDto;
import vn.techmaster.ecommecerapp.model.dto.DiscountCampaignNormalDto;
import vn.techmaster.ecommecerapp.model.projection.DiscountCampaignPublic;
import vn.techmaster.ecommecerapp.model.request.CreateDiscountCampaignRequest;
import vn.techmaster.ecommecerapp.model.request.UpdateDiscountCampaingRequest;
import vn.techmaster.ecommecerapp.repository.DiscountCampaignRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountCampaingService {
    private final ProductRepository productRepository;
    private final DiscountCampaignRepository discountCampaignRepository;
    private final Slugify slugify;

    public List<DiscountCampaignNormalDto> getAllDiscountCampaigns() {
        return discountCampaignRepository.getAllDiscountCampaignsNormalDto();
    }

    public DiscountCampaignDto getDiscountCampaignById(Long id) {
        return discountCampaignRepository.getDiscountCampaignDtoById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy discount campaign với id = " + id));
    }

    public DiscountCampaignPublic createDiscountCampaign(CreateDiscountCampaignRequest request) {
        // create discount campaign
        DiscountCampaign discountCampaign = new DiscountCampaign();
        discountCampaign.setName(request.getName());
        discountCampaign.setSlug(slugify.slugify(request.getName()));
        discountCampaign.setDescription(request.getDescription());
        discountCampaign.setDiscountType(request.getDiscountType());
        discountCampaign.setDiscountValue(request.getDiscountValue());
        discountCampaign.setStartDate(request.getStartDate());
        discountCampaign.setEndDate(request.getEndDate());
        discountCampaignRepository.save(discountCampaign);

        return DiscountCampaignPublic.of(discountCampaign);
    }

    @Transactional
    public DiscountCampaignPublic updateDiscountCampaign(Long id, UpdateDiscountCampaingRequest request) {
        DiscountCampaign discountCampaign = discountCampaignRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy discount campaign với id = " + id));

        discountCampaign.setName(request.getName());
        discountCampaign.setSlug(slugify.slugify(request.getName()));
        discountCampaign.setDescription(request.getDescription());
        discountCampaign.setDiscountType(request.getDiscountType());
        discountCampaign.setDiscountValue(request.getDiscountValue());
        discountCampaign.setStartDate(request.getStartDate());
        discountCampaign.setEndDate(request.getEndDate());

        discountCampaignRepository.deleteProductsByCampaignId(id);
        Set<Product> newProducts = new HashSet<>(productRepository.findByProductIdIn(request.getProductIds()));
        newProducts.forEach(product -> product.setDiscounts(Set.of(discountCampaign)));
        discountCampaign.setProducts(newProducts);

        discountCampaignRepository.save(discountCampaign);
        return DiscountCampaignPublic.of(discountCampaign);
    }

    public void deleteDiscountCampaign(Long id) {
        DiscountCampaign discountCampaign = discountCampaignRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy discount campaign với id = " + id));
        discountCampaignRepository.delete(discountCampaign);
    }
}
