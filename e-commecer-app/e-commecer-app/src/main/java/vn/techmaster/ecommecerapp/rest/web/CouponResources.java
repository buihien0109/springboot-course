package vn.techmaster.ecommecerapp.rest.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.techmaster.ecommecerapp.service.CouponService;

@RestController
@RequestMapping("/api/v1/coupons")
public class CouponResources {

    private final CouponService couponService;

    public CouponResources(CouponService couponService) {
        this.couponService = couponService;
    }

    @RequestMapping("/check")
    public ResponseEntity<?> checkCoupon(@RequestParam String couponCode) {
        return ResponseEntity.ok(couponService.checkCouponValid(couponCode));
    }
}
