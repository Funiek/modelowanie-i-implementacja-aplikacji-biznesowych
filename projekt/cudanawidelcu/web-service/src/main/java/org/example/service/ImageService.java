package org.example.service;

import org.example.request.ImagesRenameRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;

public interface ImageService {

    void sendImage(MultipartFile file, String name) throws IOException;

    ResponseEntity<Resource> getImage(String name);

    void renameImage(String token, ImagesRenameRequest imagesRenameRequest);
}