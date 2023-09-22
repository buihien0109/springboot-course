package vn.techmaster.ecommecerapp.rest.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.techmaster.ecommecerapp.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductResources {

    private final ProductService productService;

    public ProductResources(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/load-more")
    public ResponseEntity<?> loadMore(
            @RequestParam(required = false, defaultValue = "") String categorySlug,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "8") Integer size) {
        return ResponseEntity.ok(productService.loadMoreProduct(categorySlug, page, size));
    }
}
