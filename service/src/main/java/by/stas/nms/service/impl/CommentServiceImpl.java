package by.stas.nms.service.impl;

import by.stas.nms.cache.CustomCacheManager;
import by.stas.nms.dto.CommentDto;
import by.stas.nms.entity.Comment;
import by.stas.nms.exception.EmptyObjectPassedException;
import by.stas.nms.exception.ExceptionHolder;
import by.stas.nms.exception.IncorrectParameterException;
import by.stas.nms.exception.NoSuchElementException;
import by.stas.nms.mapper.CommentMapper;
import by.stas.nms.renovator.Renovator;
import by.stas.nms.repository.CommentRepository;
import by.stas.nms.repository.NewsRepository;
import by.stas.nms.service.CRUDService;
import by.stas.nms.service.CommentService;
import by.stas.nms.validator.CommentDtoValidator;
import by.stas.nms.validator.StringsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static by.stas.nms.exception.ExceptionMessageKey.*;

/**
 * The class {@code CommentServiceImpl} provides method to work with comments.
 *
 * @see CommentService
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final static String COMMENTS_CACHE_NAME = "comments";

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final Renovator<CommentDto> commentRenovator;
    private final CustomCacheManager cacheManager;

    @Override
    public CommentDto create(CommentDto object) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        CommentDtoValidator.isCommentCreateDtoValid(object, exceptionHolder);
        if (!exceptionHolder.getExceptionMessages().isEmpty()) {
            throw new IncorrectParameterException(exceptionHolder);
        }

        newsRepository.findNewsById(object.getNewsId()).orElseThrow(
                () -> new NoSuchElementException(NEWS_NOT_FOUND)
        );
        Comment commentToCreate = CommentMapper.INSTANCE.mapToEntity(object);
        //In order to create new document with auto-generated _id id field must be null.
        commentToCreate.setId(null);
        //Set create date to comment
        commentToCreate.setDate(LocalDateTime.now());
        Comment createdComment = commentRepository.save(commentToCreate);

        //Invalidate cache due to new object creation
        cacheManager.invalidateCacheMap(COMMENTS_CACHE_NAME);

        return CommentMapper.INSTANCE.mapToDto(createdComment);
    }

    @Override
    public List<CommentDto> readAll(Integer page, Integer limit) {
        String key = cacheManager.generateKeyForCacheMap(page, limit);

        Object[] cachedComments = cacheManager.getCollectionFromCacheMap(COMMENTS_CACHE_NAME, key);
        List<CommentDto> commentDtos;
        if (Objects.nonNull(cachedComments)) {
            commentDtos = Arrays.stream(cachedComments)
                    .map(object -> (CommentDto) object)
                    .toList();
        } else {
            PageRequest pageRequest = PageRequest.of(page, limit);
            commentDtos = commentRepository.findAll(pageRequest)
                    .getContent()
                    .stream()
                    .map(CommentMapper.INSTANCE::mapToDto)
                    .toList();

            cacheManager.putCollectionToCacheMap(COMMENTS_CACHE_NAME, key, commentDtos.toArray());
        }

        return commentDtos;
    }

    @Override
    public List<CommentDto> readAll(String term, Integer page, Integer limit) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        StringsValidator.isTermStringValid(term, exceptionHolder);
        if (!exceptionHolder.getExceptionMessages().isEmpty()) {
            throw new IncorrectParameterException(exceptionHolder);
        }

        String key = cacheManager.generateKeyForCacheMap(term, page, limit);

        Object[] cachedComments = cacheManager.getCollectionFromCacheMap(COMMENTS_CACHE_NAME, key);
        List<CommentDto> commentDtos;
        if (Objects.nonNull(cachedComments)) {
            commentDtos = Arrays.stream(cachedComments)
                    .map(object -> (CommentDto) object)
                    .toList();
        } else {
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "score"));
            TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(term);
            commentDtos = commentRepository.findAllBy(criteria, pageRequest)
                    .stream()
                    .map(CommentMapper.INSTANCE::mapToDto)
                    .toList();

            cacheManager.putCollectionToCacheMap(COMMENTS_CACHE_NAME, key, commentDtos.toArray());
        }

        return commentDtos;
    }

    @Override
    public CommentDto readById(String id) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        StringsValidator.isIdStringValid(id, exceptionHolder);
        if (!exceptionHolder.getExceptionMessages().isEmpty()) {
            throw new IncorrectParameterException(exceptionHolder);
        }

        String key = cacheManager.generateKeyForCacheMap(id);

        Optional<Object> cachedComment = cacheManager.getObjectFromCacheMap(COMMENTS_CACHE_NAME, key);
        CommentDto commentDto;
        if (cachedComment.isPresent()) {
            commentDto = (CommentDto) cachedComment.get();
        } else {
            Optional<Comment> optionalComment = commentRepository.findCommentById(id);
            Comment foundComment = optionalComment.orElseThrow(
                    () -> new NoSuchElementException(COMMENT_NOT_FOUND)
            );

            commentDto = CommentMapper.INSTANCE.mapToDto(foundComment);

            cacheManager.putObjectToCacheMap(COMMENTS_CACHE_NAME, key, commentDto);
        }

        return commentDto;
    }

    @Override
    @Transactional(value = "mongoTransactionManager", propagation = Propagation.REQUIRED)
    public CommentDto update(String id, CommentDto object) {
        if (!isUpdateObjectContainNewValues(object)) {
            throw new EmptyObjectPassedException(EMPTY_COMMENT_PASSED);
        }

        CommentDto existingComment = readById(id);
        object.setId(id);
        commentRenovator.updateObject(object, existingComment);

        ExceptionHolder exceptionHolder = new ExceptionHolder();
        CommentDtoValidator.isCommentUpdateDtoValid(object, exceptionHolder);
        if (!exceptionHolder.getExceptionMessages().isEmpty()) {
            throw new IncorrectParameterException(exceptionHolder);
        }

        Comment commentToUpdate = CommentMapper.INSTANCE.mapToEntity(object);
        Comment updatedComment = commentRepository.save(commentToUpdate);

        //Invalidate cache due to object update
        cacheManager.invalidateCacheMap(COMMENTS_CACHE_NAME);

        return CommentMapper.INSTANCE.mapToDto(updatedComment);
    }

    @Override
    public void delete(String id) {
        //Check whether comment with this id exist or not
        readById(id);

        commentRepository.deleteCommentById(id);

        //Invalidate cache due to object removing
        cacheManager.invalidateCacheMap(COMMENTS_CACHE_NAME);
    }

    private boolean isUpdateObjectContainNewValues(CommentDto object) {
        return Objects.nonNull(object) && (
                Objects.nonNull(object.getDate())
                        || Objects.nonNull(object.getText())
                        || Objects.nonNull(object.getUsername()));
    }
}
