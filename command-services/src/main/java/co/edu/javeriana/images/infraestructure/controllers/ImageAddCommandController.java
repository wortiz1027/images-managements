package co.edu.javeriana.images.infraestructure.controllers;

import co.edu.javeriana.images.application.ImagesCommandService;
import co.edu.javeriana.images.domain.Image;
import co.edu.javeriana.images.domain.Status;
import co.edu.javeriana.images.dto.Request;
import co.edu.javeriana.images.dto.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Api(value="Registro de imagenes ofrecidos por toures balon")
public class ImageAddCommandController {

    @Value("${images.resources.url}")
    private String urlTemplate;

    @Value("${images.resources.host}")
    private String imageHost;

    private final ImagesCommandService service;

    @ApiOperation(value = "Creacion de imagenes en el sistema", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Creacion exitosa de la imagen"),
            @ApiResponse(code = 400, message = "Error en los datos de entrada no se envio informacion de la imagen"),
            @ApiResponse(code = 500, message = "Error interno en el servidor, contacte y reporte con el administrador")
    })
    @PostMapping("/images")
    public ResponseEntity<CompletableFuture<Response>> handle(@RequestBody Request data) throws ExecutionException, InterruptedException, IOException {

        if (data == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Image image = new Image();
        image.setImageId(data.getMetadata().getId());
        String name = data.getMetadata().getName();
        image.setImageName(String.format("%s_%s", data.getMetadata().getId(), name.replace(" ", "_")));
        image.setImageType(data.getMetadata().getType());
        image.setImageSize(data.getMetadata().getSize());
        //image.setImageUrl(String.format(urlTemplate, InetAddress.getLocalHost().getHostName(), String.format("%s_%s", data.getMetadata().getId(), name.replace(" ", "_")))); // http://%s:%s/images/resources/load/%s
        image.setImageUrl(String.format(urlTemplate, imageHost, String.format("%s_%s.%s", data.getMetadata().getId(), name.replace(" ", "_"), data.getMetadata().getType()))); // http://%s:%s/images/resources/load/%s

        CompletableFuture<Response> rs = service.createImage(image, data.getImage());

        if (rs.get().getStatus().equalsIgnoreCase(Status.CREATED.name()))
            return new ResponseEntity<>(rs, HttpStatus.CREATED);

        if (rs.get().getStatus().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.CONFLICT);
    }

    /*@PostMapping("/images")
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
    }*/

}
