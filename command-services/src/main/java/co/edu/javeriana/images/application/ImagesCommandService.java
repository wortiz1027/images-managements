package co.edu.javeriana.images.application;

import co.edu.javeriana.images.domain.Image;
import co.edu.javeriana.images.dto.Response;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface ImagesCommandService {

    CompletableFuture<Response> createImage(Image data, MultipartFile image);
    CompletableFuture<Response> updateImage(Image data);
    CompletableFuture<Response> deleteImage(Image data);
    Resource download(String fileName) throws Exception;

}
