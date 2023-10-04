package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.UpsertDiscountCampaignRequest;
import vn.techmaster.ecommecerapp.service.DiscountCampaingService;

@RestController
@RequestMapping("/api/v1/admin/discount-campaigns")
@RequiredArgsConstructor
public class DiscountCampaingResources {
    private final DiscountCampaingService discountCampaingService;

    @PostMapping
    public ResponseEntity<?> createDiscountCampaign(@RequestBody UpsertDiscountCampaignRequest request) {
        return ResponseEntity.ok().body(discountCampaingService.createDiscountCampaign(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiscountCampaign(@RequestBody UpsertDiscountCampaignRequest request, @PathVariable Long id) {
        return ResponseEntity.ok().body(discountCampaingService.updateDiscountCampaign(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscountCampaign(@PathVariable Long id) {
        discountCampaingService.deleteDiscountCampaign(id);
        return ResponseEntity.ok().build();
    }
}
