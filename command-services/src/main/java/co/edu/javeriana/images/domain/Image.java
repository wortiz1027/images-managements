package co.edu.javeriana.images.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Estructura para el manejo de la informacion de la imagen")
public class Image {

    @ApiModelProperty(notes = "Campo con el identificador de la imagen dentro de la solucion")
    private String imageId;

    @ApiModelProperty(notes = "Campo con el nombre del archivo de imagen")
    private String imageName;

    @ApiModelProperty(notes = "Campo con la extension del archivo de imagen")
    private String imageType;

    @ApiModelProperty(notes = "Campo con el tamanio del archivo de imagen en bytes")
    private Integer imageSize;

    @ApiModelProperty(notes = "Campo con la definicion de la url de la imagen")
    private String imageUrl;

    @ApiModelProperty(notes = "Campo con el estado")
    private String status;

}
