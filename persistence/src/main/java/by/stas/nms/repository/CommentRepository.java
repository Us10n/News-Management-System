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

public interface CommentRepository extends MongoRepository<Comment, Long> {
    List<Comment> findAllBy(TextCriteria criteria, Pageable pageable);

    Optional<Comment> findCommentById(String id);

    Page<Comment> findCommentsByNewsId(PageRequest pageRequest, String id);

    List<Comment> findCommentsByNewsId(String id);

    void deleteCommentById(String id);

    void deleteCommentsByNewsId(String id);
}
