package co.edu.javeriana.images.events;

import co.edu.javeriana.images.domain.Image;
import co.edu.javeriana.images.domain.Status;
import co.edu.javeriana.images.infraestructure.repository.Repository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EventHandler.class);

    private final Repository<Image> repository;

    @RabbitListener(queues = "${events.amqp.queue}")
    public void consumer(final Image data) {
        LOG.info("recibiendo: {}", data);

        Optional<Image> image = repository.findById(data.getImageId());

        if (data.getStatus().equalsIgnoreCase(Status.CREATED.name()) && image.isEmpty()) {
            this.repository.create(data);
        }

        if (data.getStatus().equalsIgnoreCase(Status.UPDATED.name()) && image.isPresent()) {
            this.repository.update(data);
        }

        if (data.getStatus().equalsIgnoreCase(Status.DELETED.name()) && image.isPresent()) {
            this.repository.delete(data);
        }

        LOG.info("Image with code [{}] has been saved", data.getImageId());

    }

}
