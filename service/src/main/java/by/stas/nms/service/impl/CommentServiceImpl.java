package by.stas.nms.service.impl;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.entity.Comment;
import by.stas.nms.exception.EmptyListRequestedException;
import by.stas.nms.exception.ExceptionHolder;
import by.stas.nms.exception.IncorrectParameterException;
import by.stas.nms.exception.NoSuchElementException;
import by.stas.nms.mapper.CommentMapper;
import by.stas.nms.renovator.Renovator;
import by.stas.nms.repository.CommentRepository;
import by.stas.nms.repository.NewsRepository;
import by.stas.nms.service.CommentService;
import by.stas.nms.validator.CommentDtoValidator;
import by.stas.nms.validator.StringsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static by.stas.nms.exception.ExceptionMessageKey.*;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private NewsRepository newsRepository;
    private Renovator<CommentDto> commentRenovator;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, NewsRepository newsRepository, Renovator<CommentDto> commentRenovator) {
        this.commentRepository = commentRepository;
        this.newsRepository = newsRepository;
        this.commentRenovator = commentRenovator;
    }

    @Override
    public CommentDto create(CommentDto object) {
        //Set create date to comment
        object.setDate(LocalDateTime.now());

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
        commentRepository.save(commentToCreate);

        return CommentMapper.INSTANCE.mapToDto(commentToCreate);
    }

    @Override
    public List<CommentDto> readAll(Integer page, Integer limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        List<CommentDto> commentDtos = commentRepository.findAll(pageRequest)
                .getContent()
                .stream()
                .map(CommentMapper.INSTANCE::mapToDto)
                .toList();

        if (commentDtos.isEmpty()) {
            throw new EmptyListRequestedException(COMMENT_EMPTY_LIST);
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

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "score"));
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(term);
        List<CommentDto> commentDtos = commentRepository.findAllBy(criteria, pageRequest)
                .stream()
                .map(CommentMapper.INSTANCE::mapToDto)
                .toList();

        if (commentDtos.isEmpty()) {
            throw new EmptyListRequestedException(COMMENT_EMPTY_LIST);
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

        Optional<Comment> optionalComment = commentRepository.findCommentById(id);
        Comment foundComment = optionalComment.orElseThrow(
                () -> new NoSuchElementException(COMMENT_NOT_FOUND)
        );

        return CommentMapper.INSTANCE.mapToDto(foundComment);
    }

    @Override
    public CommentDto update(CommentDto object) {
        CommentDto existingComment = readById(object.getId());
        commentRenovator.updateObject(object, existingComment);

        ExceptionHolder exceptionHolder = new ExceptionHolder();
        CommentDtoValidator.isCommentUpdateDtoValid(object, exceptionHolder);
        if (!exceptionHolder.getExceptionMessages().isEmpty()) {
            throw new IncorrectParameterException(exceptionHolder);
        }

        Comment commentToUpdate = CommentMapper.INSTANCE.mapToEntity(object);
        commentRepository.save(commentToUpdate);

        return CommentMapper.INSTANCE.mapToDto(commentToUpdate);
    }

    @Override
    public void delete(String id) {
        //Check whether comment with this id exist or not
        readById(id);

        commentRepository.deleteCommentById(id);
    }
}
