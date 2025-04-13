package com.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageStorageServiceTest {

    private ImageStorageService imageStorageService;
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        imageStorageService = new ImageStorageService();
        mockFile = mock(MultipartFile.class);

    }

    @Test
    void savePostImage_ShouldReturnImagePath() throws IOException {
        // Arrange
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");

        // Act
        String result = imageStorageService.savePostImage(1L, mockFile);

        // Assert
        assertEquals("images/post_1", result);
        verify(mockFile).transferTo(any(Path.class));
    }


    @Test
    void updatePostImage_ShouldReturnNewPathAndDeleteOld() throws IOException {
        // Arrange
        MultipartFile newImage = new MockMultipartFile("new.jpg", "new.jpg", "image/jpeg", new byte[] {1,2,3,4,5});
        String existingUrl = "images/post_1_old";
        Path uploadDir = Paths.get("uploads/images");
        Files.createDirectories(uploadDir);

        // Act
        String result = imageStorageService.updatePostImage(1L, newImage, existingUrl);

        // Assert
        assertEquals("images/post_1", result);
        assertFalse(Files.exists(Path.of(existingUrl)));
    }

}