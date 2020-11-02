package co.edu.javeriana.images.events;

import co.edu.javeriana.images.application.ImageQueryService;
import co.edu.javeriana.images.events.dtos.Request;
import co.edu.javeriana.images.events.dtos.Response;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class EventHandlerRpc {

    private static final Logger LOG = LoggerFactory.getLogger(EventHandlerRpc.class);

    private final ImageQueryService service;

    @RabbitListener(queues = "${events.rpc.queue}", concurrency = "10")
    public Response receive(Request data) throws ExecutionException, InterruptedException {
        LOG.debug("RECEIVE: {}", data);
        return service.getImageByIds(data.getIds()).get();
    }

}
