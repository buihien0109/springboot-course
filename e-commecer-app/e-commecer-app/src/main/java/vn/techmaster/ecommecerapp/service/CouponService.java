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

    // Lấy tất cả coupon khả dụng
    public List<Coupon> getAllCouponValid() {
        return couponRepository.findAllByValidFromBeforeAndValidToAfter(new Date(), new Date());
    }


    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon createCoupon(Coupon request) {
        // check coupon is exist by code
        if (couponRepository.existsByCode(request.getCode())) {
            throw new BadRequestException("Mã coupon không được trùng nhau");
        }

        // TODO: check coupon is valid date
//        Date now = new Date();
//        if (now.before(request.getValidFrom()) || now.after(request.getValidTo())) {
//            throw new BadRequestException("Thời gian không hợp lệ");
//        }

        Coupon coupon = new Coupon();
        coupon.setCode(request.getCode());
        coupon.setDiscount(request.getDiscount());
        coupon.setValidFrom(request.getValidFrom());
        coupon.setValidTo(request.getValidTo());

        return couponRepository.save(coupon);
    }

    public Coupon updateCoupon(Long id, Coupon request) {
        // find coupon by id
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Coupon không tồn tại"));

        // check coupon is exist by code and different with current coupon
        if (couponRepository.existsByCode(request.getCode()) && !coupon.getCode().equals(request.getCode())) {
            throw new BadRequestException("Mã coupon không được trùng nhau");
        }

        // TODO: check coupon is valid date
//        Date now = new Date();
//        if (now.before(request.getValidFrom()) || now.after(request.getValidTo())) {
//            throw new BadRequestException("Thời gian không hợp lệ");
//        }

        // update coupon
        coupon.setCode(request.getCode());
        coupon.setDiscount(request.getDiscount());
        coupon.setValidFrom(request.getValidFrom());
        coupon.setValidTo(request.getValidTo());

        // save coupon
        return couponRepository.save(coupon);
    }

    public void deleteCoupon(Long id) {
        // find coupon by id
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Coupon không tồn tại"));

        // delete coupon
        couponRepository.delete(coupon);
    }
}
