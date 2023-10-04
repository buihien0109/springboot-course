package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.UpsertParentCategory;
import vn.techmaster.ecommecerapp.model.request.UpsertSubCategory;
import vn.techmaster.ecommecerapp.service.CategoryService;

@RestController
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
public class CategoryResources {
    private final CategoryService categoryService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok().body("Xoá thành công");
    }

    @PostMapping("/add-parent")
    public ResponseEntity<?> addParentCategory(@RequestBody UpsertParentCategory request) {
        return ResponseEntity.ok(categoryService.addParentCategory(request));
    }

    @PostMapping("/add-sub")
    public ResponseEntity<?> addSubCategory(@RequestBody UpsertSubCategory request) {
        return ResponseEntity.ok(categoryService.addSubCategory(request));
    }

    @PutMapping("/{id}/update-parent")
    public ResponseEntity<?> updateParentCategory(@RequestBody UpsertParentCategory request, @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.updateParentCategory(id, request));
    }

    @PutMapping("/{id}/update-sub")
    public ResponseEntity<?> updateSubCategory(@RequestBody UpsertSubCategory request, @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.updateSubCategory(id, request));
    }
}
