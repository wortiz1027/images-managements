package co.edu.javeriana.images.application;

import co.edu.javeriana.images.dtos.Response;
import co.edu.javeriana.images.dtos.ResponseImage;

import java.util.concurrent.CompletableFuture;

public interface ImageQueryService {

    CompletableFuture<Response> getAllImage();
    CompletableFuture<ResponseImage> getImageById(String id);
    CompletableFuture<co.edu.javeriana.images.events.dtos.Response> getImageByIds(String ids);

}
