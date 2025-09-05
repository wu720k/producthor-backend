package com.producthor.Producthor.service.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.public.base-url:/files}")
    private String publicBase;

    private static final Set<String> ALLOWED = Set.of(
            "image/jpeg", "image/png", "image/webp"
    );

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Empty file");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported content type");
        }

        String ext = guessExtension(file);
        String filename = UUID.randomUUID() + ext;

        Path root = Path.of(uploadDir).toAbsolutePath().normalize();
        Path target = root.resolve(filename);

        try {
            Files.createDirectories(root);
            if (!target.normalize().startsWith(root)) {
                throw new SecurityException("Invalid path");
            }
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }

        return publicBase + "/" + filename;
    }

    private String guessExtension(MultipartFile file) {
        String original = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase(Locale.ROOT);
        if (original.endsWith(".jpg") || original.endsWith(".jpeg")) return ".jpg";
        if (original.endsWith(".png")) return ".png";
        if (original.endsWith(".webp")) return ".webp";

        String ct = Optional.ofNullable(file.getContentType()).orElse("");
        return switch (ct) {
            case "image/jpeg" -> ".jpg";
            case "image/png"  -> ".png";
            case "image/webp" -> ".webp";
            default -> "";
        };
    }
}
