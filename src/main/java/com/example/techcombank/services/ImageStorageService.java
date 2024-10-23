package com.example.techcombank.services;

import com.example.techcombank.exception.ApiException;
import com.example.techcombank.exception.ErrorCode;
import com.example.techcombank.models.User;
import com.example.techcombank.repositories.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements IStorage {
    @Autowired
    private UserRepository userRepository;
//    private final Path storageFolder = Paths.get("uploadFile");
//    //Constructor
//    private ImageStorageService() {
//        try {
//            Files.createDirectories(storageFolder);
//        } catch (IOException exception) {
//            throw new RuntimeException("Cannot initialize storage", exception);
//        }
//    }

    private boolean isImageFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[]{"png", "jpg", "jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }

    @Override
    public byte[] storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file");
            }
            // Kiểm tra file có phải là ảnh
            if (!isImageFile(file)) {
                throw new RuntimeException("File is not an image");
            }
            // File phải <= 5Mb
            float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
            if (fileSizeInMegabytes > 5.0f) {
                throw new RuntimeException("File must be <= 5Mb");
            }
            // Tạo tên file duy nhất
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFilename = UUID.randomUUID().toString().replace("-", "") + "." + fileExtension;
            // Chuyển đổi file thành byte[]
            byte[] imageBytes = file.getBytes();
            // Lưu ảnh và tên file vào cơ sở dữ liệu
            // Ví dụ: user.setImage(imageBytes);
            // user.setFilename(generatedFilename); // Nếu bạn có trường filename trong User
            return generatedFilename.getBytes(StandardCharsets.UTF_8); // Trả về tên file duy nhất
        } catch (IOException exception) {
            throw new RuntimeException("Failed to store file", exception);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

//    @Override
//    public Stream<Path> loadAll() {
//        try {
//            //list all files in storageFolder
//            return Files.walk(this.storageFolder, 1)
//                    .filter(path -> !path.equals(this.storageFolder))
//                    .map(this.storageFolder::relativize);
//        } catch (IOException exception) {
//            throw new RuntimeException("Failed to load store file", exception);
//        }
//    }

    @Override
    public byte[] readFile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_CANNOT_FIND));
        byte[] imageBytes = user.getImage();
        if (imageBytes == null || imageBytes.length <= 0) {
            throw new RuntimeException("No image found for user id: " + userId);
        }
        return imageBytes;
    }

    @Override
    public void deleteAllFiles() {

    }
}
