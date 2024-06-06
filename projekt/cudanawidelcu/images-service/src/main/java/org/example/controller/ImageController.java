package org.example.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.request.ChangeFileNameRequest;
import org.example.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
@Controller
@RequestMapping("api/v1/images")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> serveImage(@PathVariable("imageName") String imageName,
                                               HttpServletResponse response) throws IOException {
        InputStream resource = imageService.getImage(imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
        imageService.uploadImage(image);
        return new ResponseEntity<>("File uploaded", HttpStatus.OK);
    }

    @PostMapping(value = "/rename")
    public ResponseEntity<Resource> renameImage(@RequestBody ChangeFileNameRequest changeFileNameRequest) throws IOException {
        imageService.renameImage(changeFileNameRequest.getOldName(), changeFileNameRequest.getNewName());

        return null;
    }
}
