package co.edu.javeriana.images.infraestructure.repository;

import co.edu.javeriana.images.domain.Image;
import co.edu.javeriana.images.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository<Image> {

    @Value("${images.files.upload.directory}")
    private String FILES_PATH;

    private final JdbcTemplate template;

    @Override
    public Optional<Image> findById(String id) {
        try {
            String sql = "SELECT * FROM IMAGE WHERE IMAGE_ID = ?";
            return template.queryForObject(sql,
                    new Object[]{id},
                    (rs, rowNum) ->
                            Optional.of(new Image(
                                    rs.getString("IMAGE_ID"),
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
            return CompletableFuture.completedFuture(Status.ERROR.name());
        }
    }

    @Override
    public CompletableFuture<String> update(Image data) {
        try {
            Optional<Image> image = findById(data.getImageId());

            if (!image.isPresent()) return CompletableFuture.completedFuture(Status.NO_EXIST.name());

            File dir = new File(FILES_PATH);
            File[] matches = dir.listFiles((dir1, name) -> name.equalsIgnoreCase(image.get().getImageName()));

            if (matches[0].exists()) matches[0].renameTo(new File(String.format("%s%s", FILES_PATH, data.getImageName())));

            data.setImageUrl(image.get().getImageUrl().replace(image.get().getImageName(), data.getImageName()));

            String sql = "UPDATE IMAGE SET " +
                                    "IMAGE_NAME = ?, " +
                                    "IMAGE_TYPE = ?, " +
                                    "IMAGE_SIZE = ?, " +
                                    "IMAGE_URL = ? " +
                                    "WHERE IMAGE_ID = ?";

            template.update(sql,
                    data.getImageName(),
                    data.getImageType(),
                    data.getImageSize(),
                    data.getImageUrl(),
                    data.getImageId());

            return CompletableFuture.completedFuture(Status.UPDATED.name());
        } catch(Exception e) {
            e.printStackTrace();
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
