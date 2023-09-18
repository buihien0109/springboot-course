package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.OrderTable;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
}