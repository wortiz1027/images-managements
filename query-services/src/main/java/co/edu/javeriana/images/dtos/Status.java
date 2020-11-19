package co.edu.javeriana.images.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Estructura con la informacion del estado de la consulta")
public class Status {

    @ApiModelProperty(notes = "Campo que indica el codigo del estado de la transaccion")
    private String code;

    @ApiModelProperty(notes = "Campo que indica la descripcion de la ejecucion de la transaccion")
    private String description;

}