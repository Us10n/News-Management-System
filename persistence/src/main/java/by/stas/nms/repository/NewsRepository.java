package by.stas.nms.repository;

import by.stas.nms.entity.News;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends MongoRepository<News, Long> {
    Optional<News> findNewsById(String id);

    void deleteById(String id);
}
