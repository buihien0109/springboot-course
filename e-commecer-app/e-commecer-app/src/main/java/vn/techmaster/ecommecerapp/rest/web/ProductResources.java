package vn.techmaster.ecommecerapp.rest.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.CreateProductRequest;
import vn.techmaster.ecommecerapp.model.request.UpdateProductRequest;
import vn.techmaster.ecommecerapp.model.request.UpsertProductAttributeRequest;
import vn.techmaster.ecommecerapp.service.ProductService;

@RestController
@RequestMapping("/api/v1")
public class ProductResources {

    private final ProductService productService;

    public ProductResources(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/load-more")
    public ResponseEntity<?> loadMore(
            @RequestParam(required = false, defaultValue = "") String categorySlug,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "8") Integer size) {
        return ResponseEntity.ok(productService.loadMoreProduct(categorySlug, page, size));
    }

    @PostMapping("/admin/products")
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PutMapping("/admin/products/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(productService.updateProductById(id, request));
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok().build();
    }

    // ------------------- API for attribute -------------------
    @PostMapping("/admin/products/{productId}/attributes")
    public ResponseEntity<?> createAttribute(@RequestBody UpsertProductAttributeRequest request, @PathVariable Long productId) {
        return ResponseEntity.ok(productService.createAttribute(productId, request));
    }

    @PutMapping("/admin/products/{productId}/attributes/{attributeId}")
    public ResponseEntity<?> updateAttribute(@RequestBody UpsertProductAttributeRequest request, @PathVariable Long productId, @PathVariable Long attributeId) {
        return ResponseEntity.ok(productService.updateAttribute(productId, attributeId, request));
    }

    @DeleteMapping("/admin/products/{productId}/attributes/{attributeId}")
    public ResponseEntity<?> deleteAttribute(@PathVariable Long productId, @PathVariable Long attributeId) {
        productService.deleteAttribute(productId, attributeId);
        return ResponseEntity.ok().build();
    }
}
