package by.stas.nms.service.impl;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.entity.Comment;
import by.stas.nms.entity.News;
import by.stas.nms.exception.EmptyListRequestedException;
import by.stas.nms.exception.ExceptionHolder;
import by.stas.nms.exception.IncorrectParameterException;
import by.stas.nms.exception.NoSuchElementException;
import by.stas.nms.mapper.CommentMapper;
import by.stas.nms.repository.CommentRepository;
import by.stas.nms.repository.NewsRepository;
import by.stas.nms.service.CommentService;
import by.stas.nms.validator.CommentDtoValidator;
import by.stas.nms.validator.NewsWithCommentsDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static by.stas.nms.exception.ExceptionMessageKey.COMMENT_NOT_FOUND;
import static by.stas.nms.exception.ExceptionMessageKey.NEWS_NOT_FOUND;
import static by.stas.nms.exception.ExceptionMessageKey.BAD_ID_STRING;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private NewsRepository newsRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, NewsRepository newsRepository) {
        this.commentRepository = commentRepository;
        this.newsRepository = newsRepository;
    }

    @Override
    @Transactional
    public CommentDto create(CommentDto object) {
        //set create date
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
            throw new EmptyListRequestedException();
        }

        return commentDtos;
    }

    @Override
    public CommentDto readById(String id) {
        validateIdString(id);

        Optional<Comment> optionalComment = commentRepository.findCommentById(id);
        Comment foundComment = optionalComment.orElseThrow(
                () -> new NoSuchElementException(COMMENT_NOT_FOUND)
        );

        return CommentMapper.INSTANCE.mapToDto(foundComment);
    }

    @Override
    @Transactional
    public CommentDto update(CommentDto object) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();

        CommentDto existingComment = readById(object.getId());
        if (object.getDate() == null) object.setDate(existingComment.getDate());
        if (object.getText() == null) object.setText(existingComment.getText());
        if (object.getUsername() == null) object.setUsername(existingComment.getUsername());
        object.setNewsId(existingComment.getNewsId());

        CommentDtoValidator.isCommentUpdateDtoValid(object, exceptionHolder);
        if (!exceptionHolder.getExceptionMessages().isEmpty()) {
            throw new IncorrectParameterException(exceptionHolder);
        }

        Comment commentToUpdate = CommentMapper.INSTANCE.mapToEntity(object);
        commentToUpdate.setId(object.getId());
        commentRepository.save(commentToUpdate);

        return CommentMapper.INSTANCE.mapToDto(commentToUpdate);
    }

    @Override
    @Transactional
    public void delete(String id) {
        validateIdString(id);

        commentRepository.findCommentById(id).orElseThrow(
                () -> new NoSuchElementException(COMMENT_NOT_FOUND)
        );

        commentRepository.deleteCommentById(id);
    }

    private void validateIdString(String id){
        if (!CommentDtoValidator.isIdStringValid(id)) {
            ExceptionHolder exceptionHolder = new ExceptionHolder();
            exceptionHolder.addException(BAD_ID_STRING);
            throw new IncorrectParameterException(exceptionHolder);
        }
    }
}
