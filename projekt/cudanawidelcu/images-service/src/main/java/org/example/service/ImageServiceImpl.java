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
}
