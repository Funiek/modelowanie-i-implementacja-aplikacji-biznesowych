package org.example.service;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface ImageService {
    Mono<InputStream> getImage(String fileName);
    Mono<Void> uploadImage(MultipartFile file);
    Mono<Void> renameImage(String oldFileName, String newFileName);
}