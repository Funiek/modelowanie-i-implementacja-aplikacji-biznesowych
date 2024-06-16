package org.example.service;

import org.example.dto.RecipeDto;
import org.example.request.ImagesRenameRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Implementation of ImageService responsible for handling image-related operations.
 */
@Service
public class ImageServiceImpl implements ImageService {

    private final String IMAGES_SERVICE_URL = "http://APPLICATION-GATEWAY/images-service";
    private final RestTemplate restTemplate;
    protected Logger logger = Logger.getLogger(ImageServiceImpl.class.getName());

    public ImageServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Sends an image to the images service.
     *
     * @param file image file to be sent
     * @param name name of the image
     * @throws IOException if there's an error reading the file
     */
    public void sendImage(MultipartFile file, String name) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return name + ".jpeg";
            }
        };

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("image", resource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                IMAGES_SERVICE_URL + "/api/v1/images",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("Image uploaded successfully");
        } else {
            logger.severe("Failed to upload image. Status code: " + response.getStatusCode());
        }
    }

    /**
     * Retrieves an image from the images service.
     *
     * @param name name of the image
     * @return ResponseEntity containing the image resource
     */
    public ResponseEntity<Resource> getImage(String name) {
        return restTemplate.getForEntity(IMAGES_SERVICE_URL + "/api/v1/images/" + name, Resource.class);
    }

    /**
     * Renames an image in the images service.
     *
     * @param token               JWT token for authentication
     * @param imagesRenameRequest request object containing old and new image names
     * @throws RuntimeException if there's an error during the HTTP request
     */
    public void renameImage(String token, ImagesRenameRequest imagesRenameRequest) throws RuntimeException {
        JSONObject renameJsonObject = new JSONObject();
        try {
            renameJsonObject.put("newName", imagesRenameRequest.getNewName());
            renameJsonObject.put("oldName", imagesRenameRequest.getOldName());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(renameJsonObject.toString(), headers);

        restTemplate.exchange(
                IMAGES_SERVICE_URL + "/api/v1/images/rename",
                HttpMethod.POST,
                requestEntity,
                RecipeDto.class
        );
    }
}