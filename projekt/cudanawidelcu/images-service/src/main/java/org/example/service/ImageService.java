package org.example.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface ImageService {
    InputStream getImage(String fileName) throws FileNotFoundException;
    void uploadImage(MultipartFile file) throws IOException;
}
