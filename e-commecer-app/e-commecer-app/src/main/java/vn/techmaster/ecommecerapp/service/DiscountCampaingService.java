package vn.techmaster.ecommecerapp.service;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.model.projection.DiscountCampaignPublic;
import vn.techmaster.ecommecerapp.model.request.CreateDiscountCampaignRequest;
import vn.techmaster.ecommecerapp.model.request.UpdateDiscountCampaingRequest;
import vn.techmaster.ecommecerapp.repository.DiscountCampaignRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountCampaingService {
    private final ProductRepository productRepository;
    private final DiscountCampaignRepository discountCampaignRepository;
    private final Slugify slugify;

    public List<DiscountCampaignPublic> getAllDiscountCampaigns() {
        List<DiscountCampaign> discountCampaigns = discountCampaignRepository.findAll();
        return discountCampaigns.stream().map(DiscountCampaignPublic::of).toList();
    }

    public DiscountCampaignPublic getDiscountCampaignById(Long id) {
        DiscountCampaign discountCampaign = discountCampaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy discount campaign với id = " + id));
        return DiscountCampaignPublic.of(discountCampaign);
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
        discountCampaign.setStatus(request.getStatus());
        discountCampaignRepository.save(discountCampaign);

        return DiscountCampaignPublic.of(discountCampaign);
    }

    @Transactional
    public DiscountCampaignPublic updateDiscountCampaign(Long id, UpdateDiscountCampaingRequest request) {
        DiscountCampaign discountCampaign = discountCampaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy discount campaign với id = " + id));

        discountCampaign.setName(request.getName());
        discountCampaign.setSlug(slugify.slugify(request.getName()));
        discountCampaign.setDescription(request.getDescription());
        discountCampaign.setDiscountType(request.getDiscountType());
        discountCampaign.setDiscountValue(request.getDiscountValue());
        discountCampaign.setStartDate(request.getStartDate());
        discountCampaign.setEndDate(request.getEndDate());
        discountCampaign.setStatus(request.getStatus());

        // TODO : Chưa xóa được product trong discount campaign
        // remove all product in discount campaign
        discountCampaign.getProducts().forEach(discountCampaign::removeProduct);

        // get all product ids in request
        if(!request.getProductIds().isEmpty()) {
            Set<Product> products = productRepository.findByProductIdIn(request.getProductIds());
            // set discount campaign for all product
            products.forEach(product -> product.addDiscount(discountCampaign));

            // set discount campaign for all product
            discountCampaign.setProducts(products);
        }

        discountCampaignRepository.save(discountCampaign);
        return DiscountCampaignPublic.of(discountCampaign);
    }

    public void deleteDiscountCampaign(Long id) {
        DiscountCampaign discountCampaign = discountCampaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy discount campaign với id = " + id));
        discountCampaignRepository.delete(discountCampaign);
    }
}
