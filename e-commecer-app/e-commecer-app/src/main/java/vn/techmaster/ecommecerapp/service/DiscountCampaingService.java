package vn.techmaster.ecommecerapp.service;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.model.projection.DiscountCampaignPublic;
import vn.techmaster.ecommecerapp.model.request.UpsertDiscountCampaignRequest;
import vn.techmaster.ecommecerapp.repository.DiscountCampaignRepository;
import vn.techmaster.ecommecerapp.repository.DiscountRepository;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountCampaingService {
    private final DiscountCampaignRepository discountCampaignRepository;
    private final Slugify slugify;

    public List<DiscountCampaignPublic> getAllDiscountCampaigns() {
        List<DiscountCampaign> discountCampaigns = discountCampaignRepository.findAll();
        return discountCampaigns.stream().map(DiscountCampaignPublic::of).toList();
    }

    public DiscountCampaign getDiscountCampaignById(Long id) {
        return discountCampaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy discount campaign với id = " + id));
    }

    public DiscountCampaignPublic createDiscountCampaign(UpsertDiscountCampaignRequest request) {
        // convert start date and end date from string with pattern "MM-dd-yyyy" to Date
//        Date startDate = new Date();
//        Date endDate = new Date();
//try {
//            startDate = new java.text.SimpleDateFormat("MM-dd-yyyy").parse(request.getStartDate());
//            endDate = new java.text.SimpleDateFormat("MM-dd-yyyy").parse(request.getEndDate());
//        } catch (Exception e) {
//            log.error("Error when convert date: " + e.getMessage());
//        }

        // create discount campaign
        DiscountCampaign discountCampaign = new DiscountCampaign();
        discountCampaign.setName(request.getName());
        discountCampaign.setSlug(slugify.slugify(request.getName()));
        discountCampaign.setDescription(request.getDescription());
        discountCampaign.setStartDate(request.getStartDate());
        discountCampaign.setEndDate(request.getEndDate());
        discountCampaign.setStatus(request.getStatus());
        discountCampaignRepository.save(discountCampaign);

        return DiscountCampaignPublic.of(discountCampaign);
    }

    public DiscountCampaignPublic updateDiscountCampaign(Long id, UpsertDiscountCampaignRequest request) {
        DiscountCampaign discountCampaign = discountCampaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy discount campaign với id = " + id));

        discountCampaign.setName(request.getName());
        discountCampaign.setSlug(slugify.slugify(request.getName()));
        discountCampaign.setDescription(request.getDescription());
        discountCampaign.setStartDate(request.getStartDate());
        discountCampaign.setEndDate(request.getEndDate());
        discountCampaign.setStatus(request.getStatus());

        discountCampaignRepository.save(discountCampaign);
        return DiscountCampaignPublic.of(discountCampaign);
    }

    public void deleteDiscountCampaign(Long id) {
        DiscountCampaign discountCampaign = discountCampaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy discount campaign với id = " + id));
        discountCampaignRepository.delete(discountCampaign);
    }


}
