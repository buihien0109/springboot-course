package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.entity.Coupon;
import vn.techmaster.ecommecerapp.service.CouponService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CouponResources {
    private final CouponService couponService;

    @GetMapping("/public/coupons/check")
    public ResponseEntity<?> checkCoupon(@RequestParam String couponCode) {
        return ResponseEntity.ok(couponService.checkCouponValid(couponCode));
    }

    @PostMapping("/admin/coupons")
    public ResponseEntity<?> createCoupon(@RequestBody Coupon request) {
        return ResponseEntity.ok(couponService.createCoupon(request));
    }

    @PutMapping("/admin/coupons/{id}")
    public ResponseEntity<?> updateCoupon(@PathVariable Long id, @RequestBody Coupon request) {
        return ResponseEntity.ok(couponService.updateCoupon(id, request));
    }

    @DeleteMapping("/admin/coupons/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok().build();
    }

}
