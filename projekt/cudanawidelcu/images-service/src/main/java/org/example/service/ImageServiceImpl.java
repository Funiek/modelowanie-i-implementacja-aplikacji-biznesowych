package org.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageServiceImpl implements ImageService {
    private final String PATH = "C:\\Users\\krzys\\Documents\\Coding\\modelowanie-i-implementacja-aplikacji-biznesowych\\projekt\\cudanawidelcu\\img";
    @Override
    public InputStream getImage(String fileName) throws FileNotFoundException {
        String fullPath = PATH + File.separator + fileName;
        return new FileInputStream(fullPath);
    }

    @Override
    public void uploadImage(MultipartFile file) throws IOException {
        String fullPath = PATH + File.separator + file.getOriginalFilename();
        Files.copy(file.getInputStream(), Paths.get(fullPath));
    }

    @Override
    public void renameImage(String oldFileName, String newFileName) throws IOException {
        String oldFullPath = PATH + File.separator + oldFileName;
        String newFullPath = PATH + File.separator + newFileName;

        File oldFile = new File(oldFullPath);
        File newFile = new File(newFullPath);

        if (!oldFile.exists()) {
            throw new FileNotFoundException("File not found: " + oldFullPath);
        }

        if (newFile.exists()) {
            throw new IOException("File with the new name already exists: " + newFullPath);
        }
        System.gc();

        boolean success = oldFile.renameTo(newFile);
        if (!success) {
            throw new IOException("Failed to rename file: " + oldFullPath + " to " + newFullPath);
        }
    }
}
