package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.UpsertPaymentVoucherRequest;
import vn.techmaster.ecommecerapp.service.PaymentVoucherService;

@RestController
@RequestMapping("/api/v1/admin/payment_vouchers")
@RequiredArgsConstructor
public class PaymentVoucherResources {
    private final PaymentVoucherService paymentVoucherService;

    @PostMapping
    public ResponseEntity<?> createPaymentVoucher(@RequestBody UpsertPaymentVoucherRequest request) {
        return ResponseEntity.ok(paymentVoucherService.createPaymentVoucher(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePaymentVoucher(@PathVariable Long id, @RequestBody UpsertPaymentVoucherRequest request) {
        return ResponseEntity.ok(paymentVoucherService.updatePaymentVoucher(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaymentVoucher(@PathVariable Long id) {
        paymentVoucherService.deletePaymentVoucherById(id);
        return ResponseEntity.ok().build();
    }
}
