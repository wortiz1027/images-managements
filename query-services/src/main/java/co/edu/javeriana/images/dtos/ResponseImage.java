package co.edu.javeriana.images.dtos;

import co.edu.javeriana.images.domain.Image;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseImage implements java.io.Serializable {

    private Status status;
    private Image image;

}
