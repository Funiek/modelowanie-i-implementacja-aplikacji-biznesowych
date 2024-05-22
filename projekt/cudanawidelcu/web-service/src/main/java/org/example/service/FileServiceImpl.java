package org.example.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

@Service
public class FileServiceImpl implements FileService {
    private static final String UPLOAD_DIR = "web-service/src/main/resources/public/img/";
    private final String IMAGES_SERVICE_URL = "http://images-service";
    private final RestTemplate restTemplate;
    protected Logger logger = Logger.getLogger(FileServiceImpl.class.getName());

    public FileServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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

    public void sendImage(MultipartFile file, String name) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return name + ".jpeg";
            }
        };

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("photo", resource);

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);

//        ResponseEntity<String> response = restTemplate.exchange(
//                IMAGES_SERVICE_URL + "/upload",
//                HttpMethod.POST,
//                requestEntity,
//                String.class
//        );
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        // Create an entity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                IMAGES_SERVICE_URL,
                HttpMethod.GET,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Image uploaded successfully");
        } else {
            System.err.println("Failed to upload image. Status code: " + response.getStatusCode());
        }
    }

}
