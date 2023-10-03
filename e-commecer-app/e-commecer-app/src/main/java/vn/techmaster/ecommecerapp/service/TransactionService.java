package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Transaction;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.TransactionPublic;
import vn.techmaster.ecommecerapp.repository.TransactionRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;


    public List<TransactionPublic> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(TransactionPublic::of).toList();
    }

    public TransactionPublic getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy giao dịch với id: " + id));
        return TransactionPublic.of(transaction);
    }
}
