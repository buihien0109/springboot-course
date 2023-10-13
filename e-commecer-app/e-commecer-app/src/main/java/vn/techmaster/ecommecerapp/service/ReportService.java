package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.OrderTable;
import vn.techmaster.ecommecerapp.entity.PaymentVoucher;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.model.projection.OrderTablePublic;
import vn.techmaster.ecommecerapp.model.projection.PaymentVoucherPublic;
import vn.techmaster.ecommecerapp.repository.OrderTableRepository;
import vn.techmaster.ecommecerapp.repository.PaymentVoucherRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final OrderTableRepository orderTableRepository;
    private final PaymentVoucherRepository paymentVoucherRepository;

    private Date getStartDate(String startDateString) {
        Date startDate = Date.from(
                LocalDate.now()
                        .withDayOfMonth(1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant());

        if (startDateString != null) {
            try {
                startDate = new SimpleDateFormat("dd-MM-yyyy").parse(startDateString);
            } catch (ParseException e) {
                log.error("Error when parse start date: {}", e.getMessage());
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        startDate = calendar.getTime();
        return startDate;
    }

    private Date getEndDate(String endDateString) {
        Date endDate = Date.from(
                LocalDate.now()
                        .with(TemporalAdjusters.lastDayOfMonth())
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant());

        if (endDateString != null) {
            try {
                endDate = new SimpleDateFormat("dd-MM-yyyy").parse(endDateString);
            } catch (ParseException e) {
                log.error("Error when parse start date: {}", e.getMessage());
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        endDate = calendar.getTime();
        return endDate;
    }

    private Map<String, Date> getDate(String startDateString, String endDateString) {
        Date startDate = getStartDate(startDateString);
        Date endDate = getEndDate(endDateString);

        if (startDate.after(endDate)) {
            throw new BadRequestException("Start date phải nhỏ hơn end date");
        }

        return Map.of("start", startDate, "end", endDate);
    }

    public List<OrderTablePublic> getAllOrders(String startDateString, String endDateString) {
        Map<String, Date> dateMap = getDate(startDateString, endDateString);
        Date start = dateMap.get("start");
        Date end = dateMap.get("end");

        List<OrderTable> orderTables = orderTableRepository.findByOrderDateBetweenAndStatus(start, end, OrderTable.Status.COMPLETE);
        return orderTables.stream().map(OrderTablePublic::of).toList();
    }

    public List<PaymentVoucherPublic> getAllPaymentVouchers(String startDateString, String endDateString) {
        Map<String, Date> dateMap = getDate(startDateString, endDateString);
        Date start = dateMap.get("start");
        Date end = dateMap.get("end");

        List<PaymentVoucher> paymentVouchers = paymentVoucherRepository.findByCreatedAtBetween(start, end);
        return paymentVouchers.stream().map(PaymentVoucherPublic::of).toList();
    }

    public Integer calculateOrderRevenueAmount(String startDateString, String endDateString) {
        Map<String, Date> dateMap = getDate(startDateString, endDateString);
        Date start = dateMap.get("start");
        Date end = dateMap.get("end");

        return orderTableRepository
                .findByOrderDateBetweenAndStatus(start, end, OrderTable.Status.COMPLETE)
                .stream()
                .mapToInt(OrderTable::getTotalAmount)
                .sum();
    }

    public Integer calculateTotalPaymentVoucherAmount(String startDateString, String endDateString) {
        Map<String, Date> dateMap = getDate(startDateString, endDateString);
        Date start = dateMap.get("start");
        Date end = dateMap.get("end");

        return paymentVoucherRepository
                .findByCreatedAtBetween(start, end)
                .stream()
                .mapToInt(PaymentVoucher::getAmount)
                .sum();
    }
}
