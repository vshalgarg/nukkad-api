package com.code.monks.nukkad.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/upload")
@Slf4j
public class FileUploadController {

    @Value("${image.upload.dir}")
    private String uploadDir;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<List<String>> uploadImages(@RequestPart("images") MultipartFile[] files) {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir, fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    fileUrls.add("/images/" + fileName); // fake URL to serve later if needed
                    log.info("Saved file: {}", filePath);
                } catch (IOException e) {
                    log.error("Failed to save image: {}", file.getOriginalFilename(), e);
                    throw new RuntimeException("File upload failed");
                }
            }
        }

        return ResponseEntity.ok(fileUrls);
    }
}
