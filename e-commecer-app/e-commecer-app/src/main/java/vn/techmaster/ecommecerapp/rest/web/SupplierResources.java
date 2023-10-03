package vn.techmaster.ecommecerapp.rest.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.ecommecerapp.entity.Supplier;
import vn.techmaster.ecommecerapp.service.SupplierService;

@RestController
@RequestMapping("/api/v1/admin/suppliers")
public class SupplierResources {
    private final SupplierService supplierService;

    public SupplierResources(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // Tạo các RestfulAPI CURD cho Supplier
    @GetMapping
    public ResponseEntity<?> getAllSuppliers() {
        return ResponseEntity.ok().body(supplierService.getAllSuppliers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok().body(supplierService.getSupplierById(id));
    }

    @PostMapping
    public ResponseEntity<?> createSupplier(@RequestBody Supplier supplier) {
        return ResponseEntity.ok().body(supplierService.createSupplier(supplier));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        return ResponseEntity.ok().body(supplierService.updateSupplier(id, supplier));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplierById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/update-thumbnail")
    public ResponseEntity<?> updateThumbnail(@RequestParam("file") MultipartFile file, @PathVariable Long id) {
        return ResponseEntity.ok(supplierService.updateThumbnail(id, file));
    }
}
