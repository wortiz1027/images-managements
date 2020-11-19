package co.edu.javeriana.images.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Estructura con la informacion de las imagenes registradas")
public class Response implements java.io.Serializable {

    @ApiModelProperty(notes = "Campo que indica el codigo del estado de la transaccion")
    private Status status;

    @ApiModelProperty(notes = "Listado de imagenes registradas en toures balon")
    private List<co.edu.javeriana.images.domain.Image> images;

}
