package com.amarsalimprojects.real_estate_app.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {

    private final Path uploadDir;

    public ImageController(@Value("${app.upload.dir}") String uploadDirStr) {
        System.out.println("Upload Dir Path: " + uploadDirStr); // DEBUG
        this.uploadDir = Paths.get(uploadDirStr);
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = uploadDir.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Try to detect content type
            String contentType = Files.probeContentType(filePath);

            // Fallback if contentType is null or incorrect
            if (contentType == null || contentType.equals("text/plain")) {
                String lowerName = filename.toLowerCase();
                if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
                    contentType = MediaType.IMAGE_JPEG_VALUE;
                } else if (lowerName.endsWith(".png")) {
                    contentType = MediaType.IMAGE_PNG_VALUE;
                } else if (lowerName.endsWith(".webp")) {
                    contentType = "image/webp";
                } else if (lowerName.endsWith(".gif")) {
                    contentType = MediaType.IMAGE_GIF_VALUE;
                } else {
                    contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                }
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
