package by.stas.nms.repository;

import by.stas.nms.entity.Comment;
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

/**
 * The interface {@code NewsRepository} provides custom methods to perform with {@code News} entity in MongoDB.
 *
 * @see MongoRepository
 * @see News
 */
public interface NewsRepository extends MongoRepository<News, Long> {
    /**
     * Find entity by id.
     *
     * @param id id.
     * @return optional of entity matching the given id.
     * @see Comment
     * @see Optional
     */
    Optional<News> findNewsById(String id);

    /**
     * Delete entity by id.
     *
     * @param id id.
     * @see News
     */
    void deleteById(String id);

    /**
     * Find all entities using Fulltext search.
     *
     * @param criteria fulltext search criteria.
     * @param pageable pageable to request a paged result, can be Pageable.unpaged(), must not be null.
     * @return all entities matching the given parameters.
     *
     * @see News
     * @see Pageable
     * @see TextCriteria
     */
    List<News> findAllBy(TextCriteria criteria, Pageable pageable);
}
