package co.edu.javeriana.images.infraestructure.controllers;

import co.edu.javeriana.images.application.ImagesCommandService;
import co.edu.javeriana.images.domain.Image;
import co.edu.javeriana.images.domain.Status;
import co.edu.javeriana.images.dto.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value="Actualizacion de imagenes que han sido registradas en toures balon")
public class ImageUpdateCommandController {

    private final ImagesCommandService service;

    @ApiOperation(value = "Actualizacion de imagenes en el sistema", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Actualizacion exitosa de la imagen"),
            @ApiResponse(code = 400, message = "Error en los datos de entrada no se envio informacion de la imagen"),
            @ApiResponse(code = 500, message = "Error interno en el servidor, contacte y reporte con el administrador")
    })
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
