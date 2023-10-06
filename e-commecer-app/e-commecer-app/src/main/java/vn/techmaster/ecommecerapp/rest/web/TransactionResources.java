package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.CreateTransactionRequest;
import vn.techmaster.ecommecerapp.model.request.UpdateTransactionRequest;
import vn.techmaster.ecommecerapp.service.TransactionService;

@RestController
@RequestMapping("/api/v1/admin/transactions")
@RequiredArgsConstructor
public class TransactionResources {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody CreateTransactionRequest request) {
        return ResponseEntity.ok(transactionService.createTransaction(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@RequestBody UpdateTransactionRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, request));
    }
}
