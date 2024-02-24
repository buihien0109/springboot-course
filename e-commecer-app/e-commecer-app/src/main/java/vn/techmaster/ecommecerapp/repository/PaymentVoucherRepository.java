package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.techmaster.ecommecerapp.entity.PaymentVoucher;
import vn.techmaster.ecommecerapp.model.dto.ExpenseDto;
import vn.techmaster.ecommecerapp.model.dto.PaymentVoucherDto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PaymentVoucherRepository extends JpaRepository<PaymentVoucher, Long> {
    List<PaymentVoucher> findByCreatedAtBetween(Date start, Date end);

    @Query(nativeQuery = true, name = "getAllPaymentVouchersDtoInRangeTime")
    List<PaymentVoucherDto> getAllPaymentVouchersDtoInRangeTime(Date start, Date end);

    @Query(nativeQuery = true, name = "getAllPaymentVouchersDto")
    List<PaymentVoucherDto> getAllPaymentVouchersDto();

    @Query(nativeQuery = true, name = "getPaymentVoucherDtoById")
    Optional<PaymentVoucherDto> getPaymentVoucherDtoById(Long id);

    @Query("""
            SELECT new vn.techmaster.ecommecerapp.model.dto.ExpenseDto(
                MONTH(pv.createdAt),
                YEAR(pv.createdAt),
                SUM(pv.amount)
            )
            FROM PaymentVoucher pv
            GROUP BY MONTH(pv.createdAt), YEAR(pv.createdAt)
            ORDER BY YEAR(pv.createdAt), MONTH(pv.createdAt)
            """)
    List<ExpenseDto> findExpenseByMonth();
}