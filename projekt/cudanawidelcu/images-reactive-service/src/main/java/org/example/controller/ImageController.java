package org.example.controller;

import org.example.request.ChangeFileNameRequest;
import org.example.service.ImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/images")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<ResponseEntity<Resource>> serveImage(@PathVariable("imageName") String imageName) {
        return imageService.getImage(imageName)
                .map(inputStream -> {
                    Resource resource = new InputStreamResource(inputStream);
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG)
                            .body(resource);
                });
    }

    @PostMapping
    public Mono<ResponseEntity<String>> uploadImage(@RequestParam("image") MultipartFile image) {
        return imageService.uploadImage(image)
                .then(Mono.just(new ResponseEntity<>("File uploaded", HttpStatus.OK)));
    }

    @PostMapping(value = "/rename")
    public Mono<ResponseEntity<Void>> renameImage(@RequestBody ChangeFileNameRequest changeFileNameRequest) {
        return imageService.renameImage(changeFileNameRequest.getOldName(), changeFileNameRequest.getNewName())
                .then(Mono.just(new ResponseEntity<>(HttpStatus.OK)));
    }
}
