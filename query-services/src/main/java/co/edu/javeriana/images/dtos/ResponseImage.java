package co.edu.javeriana.images.dtos;

import co.edu.javeriana.images.domain.Image;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Estructura con la informacion de la imagen registradas")
public class ResponseImage implements java.io.Serializable {

    @ApiModelProperty(notes = "Campo que indica el codigo del estado de la transaccion")
    private Status status;

    @ApiModelProperty(notes = "Estructura del detalle de la imagen")
    private Image image;

}
