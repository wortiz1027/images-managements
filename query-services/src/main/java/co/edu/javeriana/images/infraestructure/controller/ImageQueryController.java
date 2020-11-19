package co.edu.javeriana.images.infraestructure.controller;

import co.edu.javeriana.images.application.ImageQueryService;
import co.edu.javeriana.images.domain.Status;
import co.edu.javeriana.images.dtos.Response;
import co.edu.javeriana.images.dtos.ResponseImage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Api(value="Consultas de las imagenes ofrecidas por toures balon")
public class ImageQueryController {

    private final ImageQueryService service;

    @ApiOperation(value = "Consulta de las imagenes registradas en el sistema", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Consulta exitosa de las imagenes"),
            @ApiResponse(code = 404, message = "Error no se encontro informacion de las imagenes"),
            @ApiResponse(code = 500, message = "Error interno en el servidor, contacte y reporte con el administrador")
    })
    @GetMapping(value = "/images")
    public ResponseEntity<CompletableFuture<Response>> all() throws ExecutionException, InterruptedException, UnknownHostException {
        CompletableFuture<Response> rs = service.getAllImage();

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.SUCCESS.name()))
            return new ResponseEntity<>(rs, HttpStatus.OK);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.EMPTY.name()))
            return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "Consulta de las imagenes por identificador registradas en el sistema", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Consulta exitosa de las imagenes"),
            @ApiResponse(code = 404, message = "Error no se encontro informacion de las imagenes"),
            @ApiResponse(code = 500, message = "Error interno en el servidor, contacte y reporte con el administrador")
    })
    @GetMapping(value = "/images/{id}")
    public ResponseEntity<CompletableFuture<ResponseImage>> detail(@PathVariable(required = true) String id) throws ExecutionException, InterruptedException, UnknownHostException {
        CompletableFuture<ResponseImage> rs = service.getImageById(id);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.EMPTY.name()))
            return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.OK);
    }
}
