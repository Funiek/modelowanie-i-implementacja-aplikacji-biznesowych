package org.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageServiceImpl implements ImageService {
    @Value("${img}")
    private String PATH;

    @Override
    public Mono<InputStream> getImage(String fileName) {
        return Mono.fromCallable(() -> {
            String fullPath = PATH + File.separator + fileName;
            return new FileInputStream(fullPath);
        });
    }

    @Override
    public Mono<Void> uploadImage(FilePart filePart) {
        Path path = Paths.get(PATH + File.separator + filePart.filename());
        return filePart.transferTo(path);
    }

    @Override
    public Mono<Void> renameImage(String oldFileName, String newFileName) {
        return Mono.fromRunnable(() -> {
            try {
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
