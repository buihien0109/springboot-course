package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.OrderRequest;
import vn.techmaster.ecommecerapp.service.OrderService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderResources {
    private final OrderService orderService;

    @GetMapping("/public/orders/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping("/public/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    @PutMapping("/users/orders/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok().body("Hủy đơn hàng thành công");
    }

    // ==================== ADMIN ====================
    // admin update order info
    @PutMapping("/admin/orders/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.updateOrderByAdmin(id, orderRequest));
    }

    // admin cancel order
    @PutMapping("/admin/orders/{id}/cancel")
    public ResponseEntity<?> cancelOrderByAdmin(@PathVariable Long id) {
        orderService.cancelOrderByAdmin(id);
        return ResponseEntity.ok().body("Hủy đơn hàng thành công");
    }
}
