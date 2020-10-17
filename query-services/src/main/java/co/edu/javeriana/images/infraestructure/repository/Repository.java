package co.edu.javeriana.images.infraestructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Repository<T> {

    Optional<List<T>> findByAll();
    Optional<T> findById(String id);
    Optional<List<T>> findByIds(String ids);
    CompletableFuture<String> create(T data);
    CompletableFuture<String> update(T data);
    CompletableFuture<String> delete(T data);

}
