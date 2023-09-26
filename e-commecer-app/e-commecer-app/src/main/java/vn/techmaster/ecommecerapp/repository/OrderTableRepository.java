package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.OrderTable;

import java.util.Optional;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    Optional<OrderTable> findByOrderNumber(String orderNumber);
}