package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.UpsertTransactionItemRequest;
import vn.techmaster.ecommecerapp.service.TransactionItemService;

@RestController
@RequestMapping("/api/v1/admin/transaction-items")
@RequiredArgsConstructor
public class TransactionItemResources {
    private final TransactionItemService transactionItemService;

    @PostMapping
    public ResponseEntity<?> createTransactionItem(@RequestBody UpsertTransactionItemRequest request) {
        return ResponseEntity.ok(transactionItemService.createTransactionItem(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransactionItem(@RequestBody UpsertTransactionItemRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(transactionItemService.updateTransactionItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransactionItem(@PathVariable Long id) {
        transactionItemService.deleteTransactionItem(id);
        return ResponseEntity.ok().build();
    }
}
