package com.myblog.service;

import com.myblog.service.ImageStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        assertEquals("/images/post_1", result);
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