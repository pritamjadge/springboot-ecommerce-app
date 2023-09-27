package com.project.springbootangularcrud.utility;

import com.project.springbootangularcrud.exception.CustomFileNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ImageUploadUtility {


    public static Path productImageUpload(MultipartFile imageFile, String imagePath) {
        try {
            // Create the upload directory if it does not exist
            Path uploadPath = Paths.get(imagePath);
            File directory = uploadPath.toFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save the uploaded file
            Path filePath = uploadPath.resolve(Objects.requireNonNull(imageFile.getOriginalFilename()));
            imageFile.transferTo(filePath.toFile());
            return filePath;

        } catch (IOException e) {
            throw new CustomFileNotFoundException("Product Image upload failed: " + e.getMessage());
        }
    }
}
