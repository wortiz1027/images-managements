package co.edu.javeriana.images.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Image {

    private String imageId;
    private String imageName;
    private String imageType;
    private Integer imageSize;
    private String imageUrl;
    private String status;

}
