package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.entity.Banner;
import vn.techmaster.ecommecerapp.model.projection.BannerPublic;
import vn.techmaster.ecommecerapp.model.request.SortBannerRequest;
import vn.techmaster.ecommecerapp.model.request.UpsertBannerRequest;
import vn.techmaster.ecommecerapp.service.BannerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/banners")
@RequiredArgsConstructor
public class BannerResources {
    private final BannerService bannerService;

    @PostMapping
    public ResponseEntity<?> createBanner(@RequestBody UpsertBannerRequest request) {
        BannerPublic banner = bannerService.createBanner(request);
        return ResponseEntity.ok(banner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBanner(@PathVariable Integer id, @RequestBody UpsertBannerRequest request) {
        BannerPublic banner = bannerService.updateBanner(id, request);
        return ResponseEntity.ok(banner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBanner(@PathVariable Integer id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/sort")
    public ResponseEntity<?> sortBanners(@RequestBody List<Integer> request) {
        bannerService.sortBanners(request);
        return ResponseEntity.ok().build();
    }

}
