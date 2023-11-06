package com.example.demo.user.management.service.impl;

import com.example.demo.user.management.dao.UserDAO;
import com.example.demo.user.management.exception.BadRequestException;
import com.example.demo.user.management.exception.ResourceNotFoundException;
import com.example.demo.user.management.model.User;
import com.example.demo.user.management.service.FileService;
import com.example.demo.user.management.utils.Utils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {
    private static String uploadDir = System.getProperty("user.dir")
            .concat(File.separator)
            .concat("uploads");
    private final UserDAO userDAO;

    public FileServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
        createFolder(uploadDir);
    }

    public void createFolder(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public List<String> getFiles(Integer userId) {
        // Lấy đường dẫn file tương ứng với user_id
        File userDir = new File(uploadDir.concat(File.separator).concat(String.valueOf(userId)));
        if (!userDir.exists()) {
            return new ArrayList<>();
        }

        // Lấy danh sách file trong folder
        File[] files = userDir.listFiles();

        // Lấy danh sách tên file
        return Arrays.stream(files)
                .map(File::getName)
                .sorted(Comparator.reverseOrder())
                .map(fileName -> "/api/v1/users/".concat(String.valueOf(userId)).concat("/files/").concat(fileName))
                .collect(Collectors.toList());
    }

    @Override
    public String uploadFile(Integer userId, MultipartFile file) {
        // Kiểm tra user id
        User user = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + userId));

        // Tạo folder tương ứng với user id
        String userDir = uploadDir.concat(File.separator).concat(String.valueOf(userId));
        createFolder(userDir);

        // Validate file
        validateFile(file);

        // Tạo path tương ứng với file Upload lên
        String fileName = String.valueOf(Instant.now().getEpochSecond());
        File newFile = new File(userDir.concat(File.separator).concat(fileName));

        try {
            file.transferTo(newFile);
            return "/api/v1/users/".concat(String.valueOf(user.getId())).concat("/files/").concat(fileName);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi upload file");
        }
    }

    // Một số validate với file
    public void validateFile(MultipartFile file) {
        // Kiểm tra file
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new BadRequestException("Tên file không hợp lệ");
        }

        // Lấy extension file
        String fileExtension = Utils.getFileExtension(fileName);

        // Kiểm tra extension file có hợp lệ hay không
        if (!Utils.checkFileExtension(fileExtension)) {
            throw new BadRequestException("File không hợp lệ");
        }

        // Check size file
        if ((double) file.getSize() / 1_000_000L > 2) {
            throw new BadRequestException("File không được vượt quá 2MB");
        }
    }

    @Override
    public byte[] readFile(Integer userId, String fileName) {
        File file = getFile(userId, fileName);

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new BadRequestException("Lỗi khi đọc file");
        }
    }

    @Override
    public void deleteFile(Integer userId, String fileName) {
        // Lấy đường dẫn file tương ứng với user_id
        File file = getFile(userId, fileName);

        file.delete();
    }

    @Override
    public void deleteAllFiles(Integer userId) {
        // Lấy đường dẫn file tương ứng với user_id
        File userDir = new File(uploadDir.concat(File.separator).concat(String.valueOf(userId)));
        if (userDir.exists()) {
            userDir.delete();
        }
    }

    private static File getFile(Integer userId, String fileName) {
        // Lấy đường dẫn file tương ứng với user_id
        File userDir = new File(uploadDir.concat(File.separator).concat(String.valueOf(userId)));
        if (!userDir.exists()) {
            throw new BadRequestException(fileName + " không tồn tại");
        }

        // Kiểm tra đường dẫn file có tồn tại hay không
        File file = new File(userDir.getAbsolutePath().concat(File.separator).concat(fileName));
        if (!file.exists()) {
            throw new BadRequestException(fileName + " không tồn tại");
        }
        return file;
    }
}
