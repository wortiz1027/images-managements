package co.edu.javeriana.images.infraestructure.repository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ImageRepository<T> {

    Optional<T> findById(String id);
    CompletableFuture<String> create(T data);
    CompletableFuture<String> update(T data);
    CompletableFuture<String> delete(T data);

}
