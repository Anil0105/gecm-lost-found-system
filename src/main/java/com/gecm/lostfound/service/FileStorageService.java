package com.gecm.lostfound.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final long MAX_SIZE = 2L * 1024 * 1024;

    private final Path uploadDir;

    public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("Image must be 2 MB or smaller.");
        }

        String contentType = file.getContentType();
        String extension = extensionForContentType(contentType);
        if (extension == null) {
            extension = extensionFromFilename(file.getOriginalFilename());
        }
        if (extension == null) {
            throw new IllegalArgumentException("Only JPG, PNG, and WEBP images are allowed.");
        }

        Files.createDirectories(uploadDir);
        String filename = "img_" + UUID.randomUUID() + "." + extension;
        Path target = uploadDir.resolve(filename);
        file.transferTo(target);
        return filename;
    }

    public Path getUploadDir() {
        return uploadDir;
    }

    private String extensionForContentType(String contentType) {
        if (contentType == null) {
            return null;
        }
        return switch (contentType.toLowerCase(Locale.ROOT)) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> null;
        };
    }

    private String extensionFromFilename(String filename) {
        if (filename == null || !filename.contains(".")) {
            return null;
        }
        String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
        if ("jpeg".equals(ext)) {
            return "jpg";
        }
        return ALLOWED_EXTENSIONS.contains(ext) ? ext : null;
    }
}
