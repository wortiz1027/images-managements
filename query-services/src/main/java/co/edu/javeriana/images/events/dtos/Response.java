package co.edu.javeriana.images.events.dtos;

import co.edu.javeriana.images.dtos.Image;
import co.edu.javeriana.images.dtos.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response implements java.io.Serializable {

    private Status status;
    private List<Image> images;

}
