package co.edu.javeriana.images.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Estructura para el manejo de la informacion de metadata de la imagen")
public class Metadata implements java.io.Serializable {

    @ApiModelProperty(notes = "Campo con el identificador de la imagen dentro de la solucion")
    private String  id;

    @ApiModelProperty(notes = "Campo con el nombre del archivo de imagen")
    private String  name;

    @ApiModelProperty(notes = "Campo con la extension del archivo de imagen")
    private String  type;

    @ApiModelProperty(notes = "Campo con el tamanio del archivo de imagen en bytes")
    private Integer size;

}