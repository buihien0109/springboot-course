package vn.techmaster.ecommecerapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.response.ImageResponse;
import vn.techmaster.ecommecerapp.security.SecurityUtils;
import vn.techmaster.ecommecerapp.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class FileServerService {
    public static final String uploadDir = "image_uploads";

    public FileServerService() {
        FileUtils.createDirectory(uploadDir);
    }

    public List<String> getAllFilesForLoggedInUser() {
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            throw new BadRequestException("Lỗi xác thực");
        }

        // Lấy đường dẫn file tương ứng với user_id
        Path userPath = Path.of(uploadDir, String.valueOf(user.getUserId()));

        // Kiểm tra đường dẫn file có tồn tại hay không
        // Nếu không tồn tại -> user chưa upload ảnh -> trả về danh sách rỗng
        if (!Files.exists(userPath)) {
            return new ArrayList<>();
        }

        // Lấy ds file trong folder
        List<File> files = List.of(Objects.requireNonNull(userPath.toFile().listFiles()));

        // Tra về ds đường dẫn với từng file
        return files
                .stream()
                .map(File::getName)
                .sorted(Comparator.reverseOrder())
                .map(file -> "/" + uploadDir + "/" + user.getUserId() + "/" + file)
                .toList();
    }

    public ImageResponse uploadFile(MultipartFile file) {
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            throw new BadRequestException("Lỗi xác thực");
        }
        // Tạo folder tương ứng với userId
        Path userPath = Path.of(uploadDir, user.getUserId().toString());
        FileUtils.createDirectory(userPath.toString());

        // Validate file
        validateFile(file);

        // Lưu file vào folder
        String fileName = String.valueOf(System.currentTimeMillis());


        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = Path.of(userPath.toString(), fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return new ImageResponse("/" + uploadDir + "/" + user.getUserId() + "/" + fileName);
        } catch (IOException e) {
            log.error("Không thể lưu file");
            log.error(e.getMessage());
        }
        return null;
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

    public void deleteFile(String fileId) {
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            throw new BadRequestException("Lỗi xác thực");
        }

        // Lấy ra đường dẫn file tương ứng với user_id
        Path userPath = Path.of(uploadDir, String.valueOf(user.getUserId()));

        // Kiểm tra folder chứa file có tồn tại hay không
        if (!Files.exists(userPath)) {
            throw new ResouceNotFoundException("File " + fileId + " không tồn tại");
        }

        // Lấy ra đường dẫn file tương ứng với user_id và file_id
        Path file = userPath.resolve(fileId);

        // Kiểm tra file có tồn tại hay không
        if (!file.toFile().exists()) {
            throw new ResouceNotFoundException("File " + fileId + " không tồn tại");
        }

        // Tiến hành xóa file
        file.toFile().delete();
    }
}