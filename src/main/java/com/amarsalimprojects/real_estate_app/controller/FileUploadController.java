package com.amarsalimprojects.real_estate_app.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/uploads")
public class FileUploadController {

    private final String UPLOAD_DIR;

    public FileUploadController(@Value("${app.upload.dir}") String uploadDirStr) {
        this.UPLOAD_DIR = uploadDirStr;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        List<String> filenames = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No files uploaded"));
        }

        for (MultipartFile file : files) {
            try {
                if (file.isEmpty()) {
                    continue;
                }

                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || originalFilename.trim().isEmpty()) {
                    continue;
                }

                String extension = "";
                int dotIndex = originalFilename.lastIndexOf('.');
                if (dotIndex != -1) {
                    extension = originalFilename.substring(dotIndex);
                }

                if (extension.isBlank()) {
                    continue;
                }

                String uniqueFilename = UUID.randomUUID() + extension;
                Path uploadPath = Paths.get(UPLOAD_DIR);
                Files.createDirectories(uploadPath);

                Path filePath = uploadPath.resolve(uniqueFilename);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                filenames.add(uniqueFilename);

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to upload: " + e.getMessage()));
            }
        }

        return ResponseEntity.ok(Map.of("filenames", filenames));
    }
}
