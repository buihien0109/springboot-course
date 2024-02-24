package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.PaymentVoucher;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.repository.PaymentVoucherRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
public class PaymentVoucherTests {
    @Autowired
    private PaymentVoucherRepository paymentVoucherRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void save_payment_vouchers() {
        User user = userRepository.findById(1L).get();

        Faker faker = new Faker();
        for (int i = 0; i < 10; i++) {
            PaymentVoucher paymentVoucher = new PaymentVoucher();
            paymentVoucher.setPurpose(faker.lorem().sentence());
            paymentVoucher.setNote(faker.lorem().paragraph());
            paymentVoucher.setAmount(randomPrice());
            paymentVoucher.setUser(user);
            paymentVoucherRepository.save(paymentVoucher);
        }
    }

    @Test
    void update_created_at_payment_voucher() {
        List<PaymentVoucher> paymentVouchers = paymentVoucherRepository.findAll();
        Date start = new Calendar.Builder().setDate(2023, 11, 20).build().getTime();
        Date end = new Date();
        for (PaymentVoucher paymentVoucher : paymentVouchers) {
            paymentVoucher.setCreatedAt(randomDateBetweenTwoDates(start, end));
            paymentVoucherRepository.save(paymentVoucher);
        }
    }

    public int randomPrice() {
        Random random = new Random();
        int price = random.nextInt(1000000 - 100000 + 1) + 10000;
        price = price / 1000;
        price = price * 1000;
        return price;
    }

    private Date randomDateBetweenTwoDates(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);
        return new Date(randomMillisSinceEpoch);
    }
}
