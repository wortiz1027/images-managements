package co.edu.javeriana.images.dto;

import lombok.Data;

@Data
public class Request implements java.io.Serializable {

    private Metadata metadata;
    private String  image;

}
