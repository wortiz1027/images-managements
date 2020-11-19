package co.edu.javeriana.images.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Estructura para el manejo de la informacion de la imagen")
public class Request implements java.io.Serializable {

    @ApiModelProperty(notes = "Estructura que almacena la informacion de la metadata de la imagen")
    private Metadata metadata;

    @ApiModelProperty(notes = "Campo que contiene el archivo de imagen codificado en base 64")
    private String  image;

}
