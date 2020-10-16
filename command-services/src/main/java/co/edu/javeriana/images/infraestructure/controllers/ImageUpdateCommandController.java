package co.edu.javeriana.images.infraestructure.controllers;

import co.edu.javeriana.images.application.ImagesCommandService;
import co.edu.javeriana.images.domain.Image;
import co.edu.javeriana.images.domain.Status;
import co.edu.javeriana.images.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ImageUpdateCommandController {

    private final ImagesCommandService service;

    @PutMapping("/images")
    public ResponseEntity<CompletableFuture<Response>> handle(@RequestBody Image data) throws ExecutionException, InterruptedException {
        if (data == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CompletableFuture<Response> rs = service.updateImage(data);

        if (rs.get().getStatus().equalsIgnoreCase(Status.UPDATED.name()))
            return new ResponseEntity<>(rs, HttpStatus.OK);

        if (rs.get().getStatus().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.CONFLICT);
    }

}
