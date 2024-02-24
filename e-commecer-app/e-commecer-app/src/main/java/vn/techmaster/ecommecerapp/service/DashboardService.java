package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.OrderItem;
import vn.techmaster.ecommecerapp.entity.OrderTable;
import vn.techmaster.ecommecerapp.entity.PaymentVoucher;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.model.dto.*;
import vn.techmaster.ecommecerapp.repository.BlogRepository;
import vn.techmaster.ecommecerapp.repository.OrderTableRepository;
import vn.techmaster.ecommecerapp.repository.PaymentVoucherRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {
    private final OrderTableRepository orderTableRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final PaymentVoucherRepository paymentVoucherRepository;

    public Map<String, Date> getTimeRangeInCurrentMonth() {
        // get current month
        Date firstDayOfMonth = Date.from(
                LocalDate.now()
                        .withDayOfMonth(1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant());
        Date lastDayOfMonth = Date.from(
                LocalDate.now()
                        .with(TemporalAdjusters.lastDayOfMonth())
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant());

        return Map.of("start", firstDayOfMonth, "end", lastDayOfMonth);
    }

    public long countNewOrdersInRangeTime(Date start, Date end) {
        return orderTableRepository.countByOrderDateBetween(start, end);
    }

    public long countNewUsersInRangeTime(Date start, Date end) {
        return userRepository.countByCreatedAtBetween(start, end);
    }

    public long countNewBlogsInRangeTime(Date start, Date end) {
        return blogRepository.countByCreatedAtBetween(start, end);
    }

    public Integer calculateOrderRevenueInRangeTime(Date start, Date end) {
        return orderTableRepository
                .findByOrderDateBetweenAndStatus(start, end, OrderTable.Status.COMPLETE)
                .stream()
                .mapToInt(OrderTable::getTotalAmount)
                .sum();
    }

    public Integer calculateTotalPaymentVoucherInRangeTime(Date start, Date end) {
        return paymentVoucherRepository
                .findByCreatedAtBetween(start, end)
                .stream()
                .mapToInt(PaymentVoucher::getAmount)
                .sum();
    }

    public List<OrderUserDto> getOrderLatestInRangeTime(Date start, Date end) {
        return orderTableRepository.getAllOrdersInRangeTime(start, end);
    }

    public List<UserNormalDto> getUserLatestInRangeTime(Date start, Date end) {
        return userRepository.getAllUsersNormalDtoInRangeTime(start, end);
    }

    public List<Map<String, Object>> getBestSellingProductInRangeTime(Date start, Date end) {
        // get all orders in current month has status COMPLETED
        List<OrderTable> orderTables = orderTableRepository
                .findByOrderDateBetweenAndStatus(start, end, OrderTable.Status.COMPLETE);

        // get all order items in orders
        List<OrderItem> orderItems = orderTables.stream()
                .flatMap(orderTable -> orderTable.getOrderItems().stream()).toList();

        // group by product id and sum quantity
        Map<Long, Integer> productQuantityMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getProduct().getProductId()
                        , Collectors.summingInt(OrderItem::getQuantity)));

        // sort by quantity
        List<Map.Entry<Long, Integer>> sortedProductQuantityList = productQuantityMap
                .entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).toList();

        // get top product
        List<Long> topProductIdList = sortedProductQuantityList.stream().map(Map.Entry::getKey).toList();

        // get product info
        List<Product> topProductList = topProductIdList.stream()
                .map(productId -> orderItems.stream()
                        .filter(orderItem -> orderItem.getProduct().getProductId().equals(productId))
                        .findFirst()
                        .map(OrderItem::getProduct)
                        .get())
                .toList();

        // get result
        return topProductList.stream()
                .map(product -> {
                    Map<String, Object> map = Map.of(
                            "id", product.getProductId(),
                            "name", product.getName(),
                            "price", product.getPrice(),
                            "quantity", productQuantityMap.get(product.getProductId())
                    );
                    return map;
                })
                .toList();

    }


    // count all new orders in current month
    public long countNewOrdersInCurrentMonth() {
        Map<String, Date> timeRange = getTimeRangeInCurrentMonth();
        return countNewOrdersInRangeTime(timeRange.get("start"), timeRange.get("end"));
    }

    // count all new users in current month
    public long countNewUsersInCurrentMonth() {
        Map<String, Date> timeRange = getTimeRangeInCurrentMonth();
        return countNewUsersInRangeTime(timeRange.get("start"), timeRange.get("end"));
    }

    // count all new blogs in current month
    public long countNewBlogsInCurrentMonth() {
        Map<String, Date> timeRange = getTimeRangeInCurrentMonth();
        return countNewBlogsInRangeTime(timeRange.get("start"), timeRange.get("end"));
    }

    // Calculate order revenue by current month
    public Integer calculateOrderRevenueByCurrentMonth() {
        Map<String, Date> timeRange = getTimeRangeInCurrentMonth();
        return calculateOrderRevenueInRangeTime(timeRange.get("start"), timeRange.get("end"));
    }

    // Calculate total payment voucher by current month
    public Integer calculateTotalPaymentVoucherByCurrentMonth() {
        Map<String, Date> timeRange = getTimeRangeInCurrentMonth();
        return calculateTotalPaymentVoucherInRangeTime(timeRange.get("start"), timeRange.get("end"));
    }

    // Get N order latest by current month
    public List<OrderUserDto> getOrderLatestByCurrentMonth(Integer limit) {
        Map<String, Date> timeRange = getTimeRangeInCurrentMonth();
        return getOrderLatestInRangeTime(timeRange.get("start"), timeRange.get("end"))
                .stream().limit(limit).toList();
    }

    // Get N user latest by current month
    public List<UserNormalDto> getUserLatestByCurrentMonth(Integer limit) {
        Map<String, Date> timeRange = getTimeRangeInCurrentMonth();
        return getUserLatestInRangeTime(timeRange.get("start"), timeRange.get("end"))
                .stream().limit(limit).toList();
    }

    // Take the n products with the highest sales volume in the month, only counting orders with status as COMPLETE
    // Return List<Map<String, Object>>: productId, productName, quantity
    public List<Map<String, Object>> getBestSellingProductByCurrentMonth(Integer limit) {
        Map<String, Date> timeRange = getTimeRangeInCurrentMonth();
        return getBestSellingProductInRangeTime(timeRange.get("start"), timeRange.get("end"))
                .stream().limit(limit).toList();
    }

    // Tính doanh 5 tháng gần nhất List<RevenueDto>
    public List<RevenueDto> getRevenueByMonth(Integer limit) {
        List<RevenueDto> renvenueDtoList = orderTableRepository.findRevenueByMonth();
        if (renvenueDtoList.size() > limit) {
            int start = renvenueDtoList.size() - limit;
            int end = renvenueDtoList.size();
            return renvenueDtoList.subList(start, end);
        }
        return renvenueDtoList;
    }

    // Tính chi phí của 5 tháng gần nhất List<RevenueDto>
    public List<ExpenseDto> getExpenseByMonth(Integer limit) {
        List<ExpenseDto> expenseDtoList = paymentVoucherRepository.findExpenseByMonth();
        if (expenseDtoList.size() > limit) {
            int start = expenseDtoList.size() - limit;
            int end = expenseDtoList.size();
            return expenseDtoList.subList(start, end);
        }
        return expenseDtoList;
    }

    public List<RevenueExpenseDto> getRevenueExpenseByMonth(int limit) {
        List<RevenueDto> renvenueDtoList = orderTableRepository.findRevenueByMonth();
        List<ExpenseDto> expenseDtoList = paymentVoucherRepository.findExpenseByMonth();

        // Merge 2 list -> List<RevenueExpenseDto>
        List<RevenueExpenseDto> result = new ArrayList<>(renvenueDtoList.stream()
                .map(revenueDto -> {
                    ExpenseDto expenseDto = expenseDtoList.stream()
                            .filter(expenseDto1 -> expenseDto1.getMonth() == revenueDto.getMonth() && expenseDto1.getYear() == revenueDto.getYear())
                            .findFirst()
                            .orElse(new ExpenseDto(revenueDto.getMonth(), revenueDto.getYear(), 0));
                    return new RevenueExpenseDto(revenueDto.getMonth(), revenueDto.getYear(), revenueDto.getRevenue(), expenseDto.getExpense());
                })
                .toList());

        // Sort by year and month
        result.sort((o1, o2) -> {
            if (o1.getYear() == o2.getYear()) {
                return o1.getMonth() - o2.getMonth();
            }
            return o1.getYear() - o2.getYear();
        });

        // Get the last n elements
        if (result.size() > limit) {
            int start = result.size() - limit;
            int end = result.size();
            return result.subList(start, end);
        }
        return result;
    }
}
