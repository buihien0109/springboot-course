package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.OrderItem;
import vn.techmaster.ecommecerapp.entity.OrderTable;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder_User_UserId(Long userId);

    List<OrderItem> findByOrder_User_UserIdAndOrder_Status(Long userId, OrderTable.Status status);

    Optional<OrderItem> findByOrder_OrderIdAndProduct_ProductId(Long orderId, Long productId);
}