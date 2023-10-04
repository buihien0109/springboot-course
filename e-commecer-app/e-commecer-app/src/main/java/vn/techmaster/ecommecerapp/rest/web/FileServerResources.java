package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.ecommecerapp.entity.FileServer;
import vn.techmaster.ecommecerapp.service.FileServerService;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileServerResources {
    private final FileServerService fileServerService;

    @GetMapping
    public ResponseEntity<?> getAllFiles() {
        return ResponseEntity.ok(fileServerService.getAllFilesForLoggedInUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readFile(@PathVariable Long id) {
        FileServer fileServer = fileServerService.getFileById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileServer.getType()))
                .body(fileServer.getData());
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileServerService.uploadFile(file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        fileServerService.deleteFileById(id);
        return ResponseEntity.ok().build();
    }
}
