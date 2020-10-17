package co.edu.javeriana.images.application;

import co.edu.javeriana.images.domain.Image;
import co.edu.javeriana.images.dtos.Response;
import co.edu.javeriana.images.dtos.ResponseImage;
import co.edu.javeriana.images.dtos.Status;
import co.edu.javeriana.images.infraestructure.repository.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ImageQueryServiceImpl implements ImageQueryService{

    private final Repository<Image> repository;

    @Override
    public CompletableFuture<Response> getAllImage() {
        Response response = new Response();
        Status status = new Status();
        try {
            Optional<List<co.edu.javeriana.images.domain.Image>> images = this.repository.findByAll();

            if (!images.isPresent()) {
                status.setCode(co.edu.javeriana.images.domain.Status.EMPTY.name());
                status.setDescription("There are not rows availables");
                response.setStatus(status);
                return CompletableFuture.completedFuture(response);
            }

            status.setCode(co.edu.javeriana.images.domain.Status.SUCCESS.name());
            status.setDescription("There are rows availables");
            response.setStatus(status);
            response.setImages(images.get());
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            status.setCode(co.edu.javeriana.images.domain.Status.ERROR.name());
            status.setDescription(String.format("There is an error getting images type: %s", e.getMessage()));
            response.setStatus(status);
            return CompletableFuture.completedFuture(response);
        }

    }

    @Override
    public CompletableFuture<ResponseImage> getImageById(String id) {
        ResponseImage response = new ResponseImage();
        Status status = new Status();
        try {
            Optional<co.edu.javeriana.images.domain.Image> image = this.repository.findById(id);

            if (!image.isPresent()) {
                status.setCode(co.edu.javeriana.images.domain.Status.EMPTY.name());
                status.setDescription(String.format("There is not image with id: %s", id));
                response.setStatus(status);
                return CompletableFuture.completedFuture(response);
            }

            status.setCode(co.edu.javeriana.images.domain.Status.SUCCESS.name());
            status.setDescription("There are an row available");
            response.setStatus(status);
            response.setImage(image.get());
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            status.setCode(co.edu.javeriana.images.domain.Status.ERROR.name());
            status.setDescription(String.format("There is an error getting image detail: %s", e.getMessage()));
            response.setStatus(status);
            return CompletableFuture.completedFuture(response);
        }
    }
}
