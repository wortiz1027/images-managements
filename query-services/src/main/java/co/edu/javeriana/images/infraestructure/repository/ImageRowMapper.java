package co.edu.javeriana.images.infraestructure.repository;

import co.edu.javeriana.images.domain.Image;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageRowMapper  implements RowMapper<Image> {

    @Override
    public Image mapRow(ResultSet rs, int i) throws SQLException {
        Image image = new Image();
        image.setImageId(rs.getString("IMAGE_ID"));
        image.setImageName(rs.getString("IMAGE_NAME"));
        image.setImageType(rs.getString("IMAGE_TYPE"));
        image.setImageSize(rs.getInt("IMAGE_SIZE"));
        image.setImageUrl(rs.getString("IMAGE_URL"));

        return image;
    }
}
