package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.TransactionItem;

public interface TransactionItemRepository extends JpaRepository<TransactionItem, Long> {
}