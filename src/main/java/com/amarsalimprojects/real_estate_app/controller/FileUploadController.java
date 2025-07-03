package com.amarsalimprojects.real_estate_app.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/uploads")
public class FileUploadController {

    private static final String UPLOAD_DIR = "uploads";

    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        List<String> filenames = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            try {
                // Generate unique filename to avoid conflicts
                String originalFilename = file.getOriginalFilename();
                String extension = Optional.ofNullable(originalFilename)
                        .filter(f -> f.contains("."))
                        .map(f -> f.substring(originalFilename.lastIndexOf('.')))
                        .orElse("");
                String uniqueFilename = UUID.randomUUID() + extension;

                Path uploadPath = Paths.get(UPLOAD_DIR);
                Files.createDirectories(uploadPath);

                Path filePath = uploadPath.resolve(uniqueFilename);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                filenames.add(uniqueFilename);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to upload file: " + e.getMessage()));
            }
        }

        return ResponseEntity.ok(Map.of("filenames", filenames));
    }
}
