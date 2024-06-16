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

/**
 * Service implementation for handling image-related operations.
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Value("${img}")
    private String PATH;

    /**
     * Retrieves an image as an InputStream.
     *
     * @param fileName the name of the image file to retrieve
     * @return a Mono that emits an InputStream containing the image data
     */
    @Override
    public Mono<InputStream> getImage(String fileName) {
        return Mono.fromCallable(() -> {
            String fullPath = PATH + File.separator + fileName;
            return new FileInputStream(fullPath);
        });
    }

    /**
     * Uploads an image file.
     *
     * @param filePart the FilePart representing the image file to upload
     * @return a Mono<Void> that completes when the file upload is finished
     */
    @Override
    public Mono<Void> uploadImage(FilePart filePart) {
        Path path = Paths.get(PATH + File.separator + filePart.filename());
        return filePart.transferTo(path);
    }

    /**
     * Renames an existing image file.
     *
     * @param oldFileName the current name of the image file to rename
     * @param newFileName the new name to assign to the image file
     * @return a Mono<Void> that completes when the file renaming is finished
     */
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