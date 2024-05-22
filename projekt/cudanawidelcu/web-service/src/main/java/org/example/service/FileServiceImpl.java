package org.example.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

@Service
public class FileServiceImpl implements FileService {
    private static final String UPLOAD_DIR = "web-service/src/main/resources/public/img/";
    protected Logger logger = Logger.getLogger(FileServiceImpl.class.getName());
    @Override
    public boolean saveImage(byte[] imageBytes, String name) {
        try {
            String fileName = name + ".jpeg";
            File file = new File(UPLOAD_DIR + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(imageBytes);
            fos.close();
            return true;
        } catch (IOException e) {
            logger.throwing(this.getClass().getSimpleName(), "saveImage", e);
            return false;
        }
    }
}
