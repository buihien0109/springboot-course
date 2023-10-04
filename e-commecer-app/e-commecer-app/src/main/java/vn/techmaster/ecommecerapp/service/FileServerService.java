package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.ecommecerapp.entity.FileServer;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.repository.FileServerRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServerService {
    private final FileServerRepository fileServerRepository;

    public List<FileServer> getAllFilesForLoggedInUser() {
        User user = SecurityUtils.getCurrentUserLogin();
        return fileServerRepository.findByUser_UserIdOrderByCreatedAtDesc(user.getUserId());
    }

    public FileServer uploadFile(MultipartFile file) {
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            throw new BadRequestException("Lỗi xác thực");
        }
        validateFile(file);

        try {
            FileServer fileServer = new FileServer();
            fileServer.setType(file.getContentType());
            fileServer.setData(file.getBytes());
            fileServer.setUser(user);
            fileServerRepository.save(fileServer);

            return fileServer;
        } catch (IOException e) {
            throw new RuntimeException("Có lỗi xảy ra");
        }
    }

    private void validateFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        // Tên file không được trống
        if (fileName == null || fileName.isEmpty()) {
            throw new BadRequestException("Tên file không hợp lệ");
        }

        // Type file có nằm trong ds cho phép hay không
        // avatar.png, image.jpg => png, jpg
        String fileExtension = getFileExtension(fileName);
        if (!checkFileExtension(fileExtension)) {
            throw new BadRequestException("Type file không hợp lệ");
        }

        // Kích thước size có trong phạm vi cho phép không
        double fileSize = (double) (file.getSize() / 1_048_576);
        if (fileSize > 2) {
            throw new BadRequestException("Size file không được vượt quá 2MB");
        }
    }

    public String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex == -1) {
            return "";
        }
        return fileName.substring(lastIndex + 1);
    }

    public boolean checkFileExtension(String fileExtension) {
        List<String> fileExtensions = List.of("png", "jpg", "jpeg");
        return fileExtensions.contains(fileExtension);
    }

    public FileServer getFileById(Long id) {
        return fileServerRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Not found file"));
    }

    public void deleteFileById(Long id) {
        FileServer fileServer = fileServerRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Not found file"));
        fileServerRepository.delete(fileServer);
    }

    public List<FileServer> getFilesOfCurrentUser() {
        User user = SecurityUtils.getCurrentUserLogin();
        return fileServerRepository.findByUser_UserIdOrderByCreatedAtDesc(user.getUserId());
    }

    public List<FileServer> getFilesOfUserId(Long userId) {
        return fileServerRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);
    }
}