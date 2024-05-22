package org.example.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    void sendImage(MultipartFile file, String name) throws IOException;
}
