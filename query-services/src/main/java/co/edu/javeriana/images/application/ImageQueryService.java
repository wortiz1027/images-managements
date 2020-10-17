package co.edu.javeriana.images.application;

import co.edu.javeriana.images.dtos.Response;
import co.edu.javeriana.images.dtos.ResponseImage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ImageQueryService {

    CompletableFuture<Response> getAllImage();
    CompletableFuture<ResponseImage> getImageById(String id);

}
