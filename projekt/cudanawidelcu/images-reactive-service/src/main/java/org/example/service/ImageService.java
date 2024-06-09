package org.example.service;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface ImageService {
    Mono<InputStream> getImage(String fileName);
    Mono<Void> uploadImage(FilePart filePart);
    Mono<Void> renameImage(String oldFileName, String newFileName);
}