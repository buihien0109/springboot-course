package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.PaymentVoucher;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.repository.PaymentVoucherRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;

import java.util.Random;

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

    public int randomPrice() {
        Random random = new Random();
        int price = random.nextInt(1000000 - 100000 + 1) + 10000;
        price = price / 1000;
        price = price * 1000;
        return price;
    }
}
