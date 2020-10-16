package co.edu.javeriana.images.infraestructure.controllers;

import co.edu.javeriana.images.application.ImagesCommandService;
import co.edu.javeriana.images.domain.Image;
import co.edu.javeriana.images.domain.Status;
import co.edu.javeriana.images.dto.Request;
import co.edu.javeriana.images.dto.Response;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ImageAddCommandController {

    @Value("${images.resources.url}")
    private String urlTemplate;

    private final ImagesCommandService service;

    @PostMapping(value = "/images",
                 produces = MediaType.APPLICATION_JSON_VALUE,
                 consumes = { MediaType.APPLICATION_JSON_VALUE,
                              MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CompletableFuture<Response>> handle(@RequestPart(value = "data", required = true) Request data,
                                                              @RequestPart(value = "file", required = true) MultipartFile file) throws ExecutionException, InterruptedException, UnknownHostException {

        if (data == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Image image = new Image();
        image.setImageId(data.getImageId());
        String name = file.getOriginalFilename();
        image.setImageName(String.format("%s_%s", data.getImageId(), name.replace(" ", "_")));
        image.setImageType(FilenameUtils.getExtension(file.getOriginalFilename()));
        image.setImageSize((int)file.getSize());
        image.setImageUrl(String.format(urlTemplate, InetAddress.getLocalHost().getHostName(), String.format("%s_%s", data.getImageId(), name.replace(" ", "_")))); // http://%s:%s/images/resources/load/%s

        CompletableFuture<Response> rs = service.createImage(image, file);

        if (rs.get().getStatus().equalsIgnoreCase(Status.CREATED.name()))
            return new ResponseEntity<>(rs, HttpStatus.CREATED);

        if (rs.get().getStatus().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.CONFLICT);
    }

}
