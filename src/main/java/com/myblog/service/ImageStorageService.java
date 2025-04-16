package com.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageStorageService {

    private final Path uploadDir = Paths.get("uploads/images");

    public String updatePostImage(Long postId, MultipartFile newImage, String existingImageUrl) {
        if (newImage == null || newImage.isEmpty()) {
            return existingImageUrl;
        }
        if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
            Path oldFile = Paths.get("uploads", existingImageUrl);
            try {
                Files.deleteIfExists(oldFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            String fileName = "post_" + postId;
            Path filePath = uploadDir.resolve(fileName);
            newImage.transferTo(filePath);
            return "images/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке изображения", e);
        }
    }

    public String savePostImage(Long postId, MultipartFile newImage) {
        if (newImage == null || newImage.isEmpty()) {
            return "";
        }
        try {
            String fileName = "post_" + postId;
            Path filePath = uploadDir.resolve(fileName);
            newImage.transferTo(filePath);
            return "/images/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке изображения", e);
        }
    }
}