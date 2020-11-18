package co.edu.javeriana.images.dto;

import lombok.Data;

@Data
public class Metadata implements java.io.Serializable {

    private String  id;
    private String  name;
    private String  type;
    private Integer size;

}