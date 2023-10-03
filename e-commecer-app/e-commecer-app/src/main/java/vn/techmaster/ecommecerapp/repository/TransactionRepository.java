package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}