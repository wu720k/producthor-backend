package com.producthor.Producthor.rest.controller;

import com.producthor.Producthor.service.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService storage;

    @PostMapping("/admin/upload")
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) {
        String url = storage.store(file);
        return Map.of("url", url);
    }
}