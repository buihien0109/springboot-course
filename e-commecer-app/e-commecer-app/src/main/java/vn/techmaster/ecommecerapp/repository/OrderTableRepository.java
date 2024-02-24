package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.techmaster.ecommecerapp.entity.OrderTable;
import vn.techmaster.ecommecerapp.model.dto.OrderDto;
import vn.techmaster.ecommecerapp.model.dto.OrderUserDetailDto;
import vn.techmaster.ecommecerapp.model.dto.OrderUserDto;
import vn.techmaster.ecommecerapp.model.dto.RevenueDto;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    Optional<OrderTable> findByOrderNumber(String orderNumber);

    List<OrderTable> findByUser_UserIdOrderByOrderDateDesc(Long userId);

    // count all order in current month
    long countByOrderDateBetween(Date start, Date end);

    List<OrderTable> findByOrderDateBetweenAndStatus(Date start, Date end, OrderTable.Status status);

    List<OrderTable> findByOrderDateBetween(Date start, Date end);

    @Query(nativeQuery = true, name = "getAllOrdersByUser")
    List<OrderUserDto> getAllOrdersByUser(Long userId);

    @Query(nativeQuery = true, name = "getAllOrdersDtoAdmin")
    List<OrderDto> getAllOrdersDtoAdmin();

    @Query(nativeQuery = true, name = "getAllOrdersInRangeTime")
    List<OrderUserDto> getAllOrdersInRangeTime(Date start, Date end);

    @Query(nativeQuery = true, name = "getAllOrdersInRangeTimeByStatus")
    List<OrderUserDetailDto> getAllOrdersInRangeTimeByStatus(Date start, Date end, String status);

    @Query(nativeQuery = true, name = "getRevenueByMonth")
    List<RevenueDto> findRevenueByMonth();
}