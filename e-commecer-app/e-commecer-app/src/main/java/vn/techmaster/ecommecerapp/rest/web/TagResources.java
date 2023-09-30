package vn.techmaster.ecommecerapp.rest.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.UpsertTagRequest;
import vn.techmaster.ecommecerapp.service.TagService;

@RestController
@RequestMapping("/api/v1/admin/tags")
public class TagResources {

    private final TagService tagService;

    public TagResources(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody UpsertTagRequest request) {
        return new ResponseEntity<>(tagService.createTag(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTagById(@RequestBody UpsertTagRequest request, @PathVariable Integer id) {
        return ResponseEntity.ok(tagService.updateTagById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTopicById(@PathVariable Integer id) {
        tagService.deleteTagById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/blogs")
    public ResponseEntity<?> getAllBlogsByTagId(@PathVariable Integer id) {
        return ResponseEntity.ok(tagService.getAllBlogsByTagId(id));
    }
}
