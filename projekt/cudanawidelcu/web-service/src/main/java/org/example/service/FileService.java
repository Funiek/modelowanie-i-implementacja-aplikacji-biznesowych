package org.example.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    boolean saveImage(byte[] imageBytes, String name);
    void sendImage(MultipartFile file, String name) throws IOException;
}
