package vn.techmaster.ecommecerapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.Coupon;
import vn.techmaster.ecommecerapp.repository.CouponRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@SpringBootTest
public class CouponTests {

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void save_coupons() {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plusDays(10);
        Coupon coupon1 = new Coupon(null, "AAA", 10, convertLocalDateTimeToDate(from), convertLocalDateTimeToDate(to));
        couponRepository.save(coupon1);

        Coupon coupon2 = new Coupon(null, "BBB", 20, convertLocalDateTimeToDate(from), convertLocalDateTimeToDate(to));
        couponRepository.save(coupon2);

        Coupon coupon3 = new Coupon(null, "CCC", 30, convertLocalDateTimeToDate(from), convertLocalDateTimeToDate(to));
        couponRepository.save(coupon3);

        Coupon coupon4 = new Coupon(null, "DDD", 40, convertLocalDateTimeToDate(from), convertLocalDateTimeToDate(to));
        couponRepository.save(coupon4);
    }

    // covert LocalDateTimeToDate
    private Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
