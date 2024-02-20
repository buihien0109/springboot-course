package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.techmaster.ecommecerapp.entity.Transaction;
import vn.techmaster.ecommecerapp.model.dto.TransactionNormalDto;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySupplier_SupplierIdOrderByTransactionDateDesc(Long supplierId);

    @Query(nativeQuery = true, name = "getAllTransactionsBySupplier")
    List<TransactionNormalDto> getAllTransactionsBySupplier(Long supplierId);

    @Query(nativeQuery = true, name = "getAllTransactions")
    List<TransactionNormalDto> getAllTransactions();

    @Query(nativeQuery = true, name = "getTransactionById")
    Optional<TransactionNormalDto> getTransactionById(Long id);
}