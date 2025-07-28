package com.mho_toys.backend.service.ServiceImpl;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mho_toys.backend.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
        "image/jpeg", "image/png", "image/gif", "image/webp"
    );
    
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
        ".jpg", ".jpeg", ".png", ".gif", ".webp"
    );
    
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // Validate file
        validateImageFile(file);

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty");
        }

        // Sanitize filename and ensure safe extension
        String fileExtension = getFileExtension(originalFileName);
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException("File type not allowed. Only JPG, PNG, GIF, and WebP images are supported.");
        }

        // Generate secure filename
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId + fileExtension;
        String filePath = path + File.separator + fileName;
        
        // Create directory if it doesn't exist
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Additional security: validate file content
        validateImageContent(file);

        // Save file
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Invalid file type. Only image files are allowed.");
        }
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("File must have an extension");
        }
        return fileName.substring(lastDotIndex);
    }

    private void validateImageContent(MultipartFile file) throws IOException {
        // Read first few bytes to validate file signature
        byte[] header = new byte[8];
        file.getInputStream().read(header);
        
        // Check for common image file signatures
        if (!isValidImageHeader(header)) {
            throw new IllegalArgumentException("File content does not match expected image format");
        }
    }

    private boolean isValidImageHeader(byte[] header) {
        // JPEG signature: FF D8 FF
        if (header.length >= 3 && 
            (header[0] & 0xFF) == 0xFF && 
            (header[1] & 0xFF) == 0xD8 && 
            (header[2] & 0xFF) == 0xFF) {
            return true;
        }

        // PNG signature: 89 50 4E 47 0D 0A 1A 0A
        if (header.length >= 8 &&
            (header[0] & 0xFF) == 0x89 &&
            (header[1] & 0xFF) == 0x50 &&
            (header[2] & 0xFF) == 0x4E &&
            (header[3] & 0xFF) == 0x47 &&
            (header[4] & 0xFF) == 0x0D &&
            (header[5] & 0xFF) == 0x0A &&
            (header[6] & 0xFF) == 0x1A &&
            (header[7] & 0xFF) == 0x0A) {
            return true;
        }

        // GIF signature: 47 49 46 38 (GIF8)
        if (header.length >= 4 &&
            (header[0] & 0xFF) == 0x47 &&
            (header[1] & 0xFF) == 0x49 &&
            (header[2] & 0xFF) == 0x46 &&
            (header[3] & 0xFF) == 0x38) {
            return true;
        }

        // WebP signature: 52 49 46 46 (RIFF) followed by WebP
        if (header.length >= 4 &&
            (header[0] & 0xFF) == 0x52 &&
            (header[1] & 0xFF) == 0x49 &&
            (header[2] & 0xFF) == 0x46 &&
            (header[3] & 0xFF) == 0x46) {
            return true;
        }

        return false;
    }
}
