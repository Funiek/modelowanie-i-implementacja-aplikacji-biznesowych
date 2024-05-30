package org.example.service;

import org.example.dto.ProductDto;
import org.example.dto.RecipeDto;
import org.example.dto.VoteDto;
import org.example.request.ChangeFileNameRequest;
import org.json.JSONArray;
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

@Service
public class ImageService {
    private final String IMAGES_SERVICE_URL = "http://APPLICATION-GATEWAY/images-service";
    private final RestTemplate restTemplate;
    protected Logger logger = Logger.getLogger(ImageService.class.getName());

    public ImageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


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
            System.out.println("Image uploaded successfully");
        } else {
            System.err.println("Failed to upload image. Status code: " + response.getStatusCode());
        }
    }

    public ResponseEntity<Resource> getImage(String name) {
        return restTemplate.getForEntity(IMAGES_SERVICE_URL + "/api/v1/images/" + name, Resource.class);
    }

    public void renameImage(String token, ChangeFileNameRequest changeFileNameRequest) {


        JSONObject renameJsonObject = new JSONObject();
        try {
            renameJsonObject.put("newName", changeFileNameRequest.getNewName());
            renameJsonObject.put("oldName", changeFileNameRequest.getOldName());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(renameJsonObject.toString(), headers);

        ResponseEntity<RecipeDto> responseEntity = restTemplate.exchange(
                IMAGES_SERVICE_URL + "/api/v1/images/rename",
                HttpMethod.POST,
                requestEntity,
                RecipeDto.class
        );
    }
}
