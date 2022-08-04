package by.stas.nms.repository;

import by.stas.nms.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends MongoRepository<News, Long>{
    Optional<News> findNewsById(String id);

    void deleteById(String id);

    List<News> findAllBy(TextCriteria criteria, Pageable pageable);
}
