package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.PaymentVoucher;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface PaymentVoucherRepository extends JpaRepository<PaymentVoucher, Long> {
    List<PaymentVoucher> findByCreatedAtBetween(Date start, Date end);
}