package co.edu.javeriana.images.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response implements java.io.Serializable {

    private Status status;
    private List<co.edu.javeriana.images.domain.Image> images;

}
