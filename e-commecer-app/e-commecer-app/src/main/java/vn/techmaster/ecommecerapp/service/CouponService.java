package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Coupon;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.repository.CouponRepository;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    // Lấy coupon theo code
    public Coupon checkCouponValid(String couponCode) {
        log.info("couponCode: " + couponCode);
        // Kiểm tra xem copon có hợp lệ và đã hết hạn chưa
        if(!isValidCoupon(couponCode)) throw new BadRequestException("Coupon không hợp lệ");

        return couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new ResouceNotFoundException("Coupon không hợp lệ"));
    }

    // Kiểm tra xem copon có hợp lệ và đã hết hạn chưa
    public boolean isValidCoupon(String couponCode) {
        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new ResouceNotFoundException("Coupon không hợp lệ"));


        // check coupon is valid date
        Date now = new Date();
        if (now.before(coupon.getValidFrom()) || now.after(coupon.getValidTo())) {
            throw new BadRequestException("Coupon đã hết hạn");
        }

        return true;
    }


    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }
}
