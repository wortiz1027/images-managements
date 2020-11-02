package co.edu.javeriana.images.infraestructure.repository;

import co.edu.javeriana.images.domain.Image;
import co.edu.javeriana.images.domain.Status;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ImageMySqlRepository implements Repository<Image> {

    private static final Logger LOG = LoggerFactory.getLogger(ImageMySqlRepository.class);

    private final JdbcTemplate template;

    @Override
    public Optional<List<Image>> findByAll() {
        try{
            String sql = "SELECT * FROM IMAGE";
            List<Image> types = this.template.query(sql, new ImageRowMapper());
            return Optional.of(types);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Image> findById(String id) {
        try {
            String sql = "SELECT * FROM IMAGE WHERE IMAGE_ID = ?";
            return template.queryForObject(sql,
                    new Object[]{id},
                    (rs, rowNum) ->
                            Optional.of(new Image(rs.getString("IMAGE_ID"),
                                                    rs.getString("IMAGE_NAME"),
                                                    rs.getString("IMAGE_TYPE"),
                                                    rs.getInt("IMAGE_SIZE"),
                                                    rs.getString("IMAGE_URL"),
                                                    ""
                            ))
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Image>> findByIds(String ids) {
        try{

            List<String> idents = Arrays.asList(ids.split(","));
            List<Image> images = new ArrayList<>();

            for (String id : idents) {
                String sql = "SELECT * FROM IMAGE WHERE IMAGE_ID = ?";

                //List<Image> images = this.template.query(sql, new String[]{id}, new ImageRowMapper());

                Image image = template.queryForObject(sql,
                        new Object[]{id},
                        (rs, rowNum) ->
                                new Image(rs.getString("IMAGE_ID"),
                                        rs.getString("IMAGE_NAME"),
                                        rs.getString("IMAGE_TYPE"),
                                        rs.getInt("IMAGE_SIZE"),
                                        rs.getString("IMAGE_URL"),
                                        ""
                                )
                );

                images.add(image);
            }

            LOG.info("[IMAGES]: [{}]", images);

            return Optional.of(images);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<String> create(Image data) {
        try {
            if (findById(data.getImageId()).isPresent()) return CompletableFuture.completedFuture(Status.EXIST.name());

            String sql = "INSERT INTO IMAGE (IMAGE_ID, " +
                                            "IMAGE_NAME, " +
                                            "IMAGE_TYPE, " +
                                            "IMAGE_SIZE, " +
                                            "IMAGE_URL) " +
                                            "VALUES (?,?,?,?,?)";

            template.update(sql,
                            data.getImageId(),
                            data.getImageName(),
                            data.getImageType(),
                            data.getImageSize(),
                            data.getImageUrl());

            return CompletableFuture.completedFuture(Status.CREATED.name());
        } catch(Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(Status.ERROR.name());
        }
    }

    @Override
    public CompletableFuture<String> update(Image data) {
        try {
            if (findById(data.getImageId()).isEmpty()) return CompletableFuture.completedFuture(Status.NO_EXIST.name());

            String sql = "UPDATE IMAGE SET " +
                            "IMAGE_NAME = ?, " +
                            "IMAGE_TYPE = ?, " +
                            "IMAGE_SIZE = ?, " +
                            "IMAGE_URL = ? " +
                            "WHERE IMAGE_ID = ? ";

            this.template.update(sql,
                                 data.getImageName(),
                                 data.getImageType(),
                                 data.getImageSize(),
                                 data.getImageUrl(),
                                 data.getImageId());

            return CompletableFuture.completedFuture(Status.UPDATED.name());
        } catch (Exception e) {
            return CompletableFuture.completedFuture(Status.ERROR.name());
        }
    }

    @Override
    public CompletableFuture<String> delete(Image data) {
        try {
            if (findById(data.getImageId()).isEmpty()) return CompletableFuture.completedFuture(Status.NO_EXIST.name());

            String sql = "DELETE FROM IMAGE WHERE IMAGE_ID = ?";

            this.template.update(sql, data.getImageId());

            return CompletableFuture.completedFuture(Status.DELETED.name());
        } catch (Exception e) {
            return CompletableFuture.completedFuture(Status.ERROR.name());
        }
    }
}
