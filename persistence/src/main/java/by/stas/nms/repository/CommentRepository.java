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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The interface {@code CommentRepository} provides custom methods to perform with {@code Comment} entity in MongoDB.
 *
 * @see MongoRepository
 * @see Comment
 */
public interface CommentRepository extends MongoRepository<Comment, Long> {
    /**
     * Find all entities using Fulltext search.
     *
     * @param criteria fulltext search criteria.
     * @param pageable pageable to request a paged result, can be Pageable.unpaged(), must not be null.
     * @return all entities matching the given parameters.
     *
     * @see Comment
     * @see Pageable
     * @see TextCriteria
     */
    List<Comment> findAllBy(TextCriteria criteria, Pageable pageable);

    /**
     * Find entity by id.
     *
     * @param id id.
     * @return optional of entity matching the given id.
     *
     * @see Comment
     * @see Optional
     */
    Optional<Comment> findCommentById(String id);

    /**
     * Find all comments by news id.
     *
     * @param pageRequest pageable to request a paged result, must not be null.
     * @param id          id/
     * @return page with entities matching params.
     *
     * @see Comment
     * @see PageRequest
     * @see Page
     */
    Page<Comment> findCommentsByNewsId(PageRequest pageRequest, String id);

    /**
     * Find list of entity by newsId field.
     *
     * @param id id.
     * @return list of entities matching param.
     *
     * @see Comment
     * @see List
     */
    List<Comment> findCommentsByNewsId(String id);

    /**
     * Delete entity by id.
     *
     * @param id id.
     *
     * @see Comment
     */
    void deleteCommentById(String id);

    /**
     * Delete multiple entities by news id.
     *
     * @param id news id.
     *
     * @see Comment
     */
    void deleteCommentsByNewsId(String id);
}
