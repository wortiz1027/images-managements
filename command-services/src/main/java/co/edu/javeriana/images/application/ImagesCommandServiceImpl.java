package co.edu.javeriana.images.application;

import co.edu.javeriana.images.domain.Image;
import co.edu.javeriana.images.domain.Status;
import co.edu.javeriana.images.dto.Response;
import co.edu.javeriana.images.infraestructure.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ImagesCommandServiceImpl implements ImagesCommandService {

    private static final Logger LOG = LoggerFactory.getLogger(ImagesCommandServiceImpl.class);

    private Path location;

    private String TPL_TEMPLATE_FILE_PATH = "%s%s.%s";

    @Value("${images.files.upload.directory}")
    private String FILES_PATH;

    @Value("${events.amqp.exchange}")
    String imageExchange;

    @Value("${events.amqp.routing-key}")
    String imageRoutingKey;

    private final ImageRepository<Image> repository;
    private final AmqpTemplate template;

    @Override
    public CompletableFuture<Response> createImage(Image data, String file) {
        Response response = new Response();
        try {
            String status = this.repository.create(data).get();
            response.setStatus(status);

            if (!status.equalsIgnoreCase(Status.CREATED.name())) {
                response.setDescription(String.format("The image with id: {%s} has an error", data.getImageId()));
                return CompletableFuture.completedFuture(response);
            }

            boolean isUploaded = upload(data, file);

            if (!isUploaded) {
                response.setStatus(Status.ERROR.name());
                response.setDescription(String.format("Exception uploading image has been release: {%s}", data.getImageName()));
            }

            data.setStatus(Status.CREATED.name());
            this.template.convertAndSend(imageExchange, imageRoutingKey, data);
            response.setDescription(String.format("The image with id: {%s} has been created", data.getImageId()));

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            response.setStatus(Status.ERROR.name());
            response.setDescription(String.format("Exception creating row has been release: {%s}", e.getMessage()));
            return CompletableFuture.completedFuture(response);
        }
    }

    @Override
    public CompletableFuture<Response> updateImage(Image data) {
        Response response = new Response();
        try {
            String status = this.repository.update(data).get();
            response.setStatus(status);

            if (!status.equalsIgnoreCase(Status.UPDATED.name())) {
                response.setDescription(String.format("The image with id: {%s} has an error", data.getImageId()));
                return CompletableFuture.completedFuture(response);
            }

            response.setDescription(String.format("The image with id: {%s} has been updated", data.getImageId()));
            data.setStatus(Status.UPDATED.name());
            this.template.convertAndSend(imageExchange, imageRoutingKey, data);

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            response.setStatus(Status.ERROR.name());
            response.setDescription(String.format("Exception updating row has been release: {%s}", e.getMessage()));
            return CompletableFuture.completedFuture(response);
        }
    }

    @Override
    public CompletableFuture<Response> deleteImage(Image data) {
        Response response = new Response();
        try {
            String status = this.repository.delete(data).get();
            response.setStatus(status);

            if (!status.equalsIgnoreCase(Status.DELETED.name())) {
                response.setDescription(String.format("The image with id: {%s} has an error", data.getImageId()));
                return CompletableFuture.completedFuture(response);
            }

            response.setDescription(String.format("The image with id: {%s} has been deleted", data.getImageId()));
            data.setStatus(Status.DELETED.name());
            this.template.convertAndSend(imageExchange, imageRoutingKey, data);

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            response.setStatus(Status.ERROR.name());
            response.setDescription(String.format("Exception deleting row has been release: {%s}", e.getMessage()));
            return CompletableFuture.completedFuture(response);
        }
    }

    /*private boolean upload(Image data, MultipartFile mfile) {
        try {
            File file = new File(String.format(this.TPL_TEMPLATE_FILE_PATH, this.FILES_PATH, data.getImageName()));

            if (!file.exists()) file.createNewFile();

            try ( FileOutputStream fos = new FileOutputStream(file) ) {
                fos.write(mfile.getBytes());
            }
            return Boolean.TRUE;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            LOG.error("Error convert multipartfile to file: ", ioe);
            return Boolean.FALSE;
        }
    }*/

    private boolean upload(Image data, String img) {
        try {
            byte[] image = Base64.decodeBase64(img);

            File file = new File(String.format(this.TPL_TEMPLATE_FILE_PATH, this.FILES_PATH, data.getImageName(), data.getImageType()));

            if (!file.exists()) file.createNewFile();

            try ( FileOutputStream fos = new FileOutputStream(file) ) {
                fos.write(image);
            }
            return Boolean.TRUE;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            LOG.error("Error convert multipartfile to file: ", ioe);
            return Boolean.FALSE;
        }
    }

    @Override
    public Resource download(String fileName) throws Exception {
        try {
            this.location = Paths.get(FILES_PATH).toAbsolutePath().normalize();
            Path filePath = this.location.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }

}
