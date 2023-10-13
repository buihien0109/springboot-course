package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.AdminUpsertOrderItemRequest;
import vn.techmaster.ecommecerapp.service.OrderItemService;

@RestController
@RequestMapping("/api/v1/admin/order-items")
@RequiredArgsConstructor
public class OrderItemResources {
    private final OrderItemService orderItemService;

    @PostMapping
    public ResponseEntity<?> adminCreateOrderItem(@RequestBody AdminUpsertOrderItemRequest request) {
        return ResponseEntity.ok(orderItemService.adminCreateOrderItem(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> adminUpdateOrderItem(@RequestBody AdminUpsertOrderItemRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(orderItemService.adminUpdateOrderItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> adminDeleteOrderItem(@PathVariable Long id) {
        orderItemService.adminDeleteOrderItem(id);
        return ResponseEntity.ok().build();
    }
}
