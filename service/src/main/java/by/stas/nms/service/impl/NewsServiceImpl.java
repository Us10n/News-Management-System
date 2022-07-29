package by.stas.nms.service.impl;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.dto.NewsDto;
import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.entity.Comment;
import by.stas.nms.entity.News;
import by.stas.nms.exception.EmptyListRequestedException;
import by.stas.nms.exception.ExceptionHolder;
import by.stas.nms.exception.IncorrectParameterException;
import by.stas.nms.exception.NoSuchElementException;
import by.stas.nms.mapper.CommentMapper;
import by.stas.nms.mapper.NewsMapper;
import by.stas.nms.mapper.NewsWithCommentsMapper;
import by.stas.nms.repository.CommentRepository;
import by.stas.nms.repository.NewsRepository;
import by.stas.nms.service.NewsService;
import by.stas.nms.validator.NewsWithCommentsDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static by.stas.nms.exception.ExceptionMessageKey.BAD_ID_STRING;
import static by.stas.nms.exception.ExceptionMessageKey.NEWS_NOT_FOUND;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository,
                           CommentRepository commentRepository) {
        this.newsRepository = newsRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public NewsWithCommentsDto create(NewsWithCommentsDto object) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();

        //set create date
        object.setDate(LocalDateTime.now());
        NewsWithCommentsDtoValidator.isNewsWithCommentsDtoValid(object, exceptionHolder);
        if (!exceptionHolder.getExceptionMessages().isEmpty()) {
            throw new IncorrectParameterException(exceptionHolder);
        }

        News newsToCreate = NewsWithCommentsMapper.INSTANCE.mapToEntity(object);
        newsRepository.save(newsToCreate);

        List<Comment> comments = object.getComments()
                .stream()
                .map(CommentMapper.INSTANCE::mapToEntity)
                .toList();
        commentRepository.saveAll(comments);

        return NewsWithCommentsMapper.INSTANCE.mapToDto(newsToCreate);
    }

    @Override
    public List<NewsDto> readAll(Integer page, Integer limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        List<NewsDto> newsDtos = newsRepository.findAll(pageRequest)
                .getContent()
                .stream()
                .map(NewsMapper.INSTANCE::mapToDto)
                .toList();

        if (newsDtos.isEmpty()) {
            throw new EmptyListRequestedException();
        }

        return newsDtos;
    }

    @Override
    @Transactional
    public NewsWithCommentsDto readById(String id, Integer page, Integer limit) {
        validateIdString(id);

        Optional<News> optionalNews = newsRepository.findNewsById(id);
        News foundNews = optionalNews.orElseThrow(
                () -> new NoSuchElementException(NEWS_NOT_FOUND)
        );

        PageRequest pageRequest = PageRequest.of(page, limit);
        List<Comment> foundComments = commentRepository.findCommentsByNewsId(pageRequest, id).getContent();

        NewsWithCommentsDto newsWithCommentsDto = NewsWithCommentsMapper.INSTANCE.mapToDto(foundNews);
        List<CommentDto> commentDtos = foundComments.stream().map(CommentMapper.INSTANCE::mapToDto).toList();
        newsWithCommentsDto.setComments(commentDtos);

        return newsWithCommentsDto;
    }

    @Override
    public NewsWithCommentsDto readById(String id) {
        return readById(id, 0, 10);
    }

    @Override
    @Transactional
    public NewsWithCommentsDto update(NewsWithCommentsDto object) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();

        validateIdString(object.getId());

        Optional<News> optionalNews = newsRepository.findNewsById(object.getId());
        News foundNews = optionalNews.orElseThrow(
                () -> new NoSuchElementException(NEWS_NOT_FOUND)
        );
        if (object.getDate() == null) object.setDate(foundNews.getDate());
        if (object.getText() == null) object.setText(foundNews.getText());
        if (object.getTitle() == null) object.setText(foundNews.getTitle());
        NewsWithCommentsDtoValidator.isNewsWithCommentsDtoValid(object, exceptionHolder);
        if (!exceptionHolder.getExceptionMessages().isEmpty()) {
            throw new IncorrectParameterException(exceptionHolder);
        }

        if (object.getComments().stream().allMatch(commentDto -> co))

            return null;
    }

    @Override
    @Transactional
    public void delete(String id) {
        validateIdString(id);
    }

    private void validateIdString(String id) {
        if (!NewsWithCommentsDtoValidator.isIdStringValid(id)) {
            ExceptionHolder exceptionHolder = new ExceptionHolder();
            exceptionHolder.addException(BAD_ID_STRING);
            throw new IncorrectParameterException(exceptionHolder);
        }
    }

    private List<CommentDto> replaceNewsCommentWithNewOnes(String newsId, List<CommentDto> newCommentDtos) {
        List<Comment> existingComments = commentRepository.findCommentsByNewsId(newsId);


        List<CommentDto> attachedComments = new ArrayList<>();
        newComments.parallelStream().forEach(newComment -> {
            Boolean[] alreadyExist = {false};
            existingComments.parallelStream().forEach(existingComment -> {
                if (newComment.equals(existingComment)) {
                    alreadyExist[0] = true;
                }
            });
            newComment.setNewsId(newsId);

            if (!alreadyExist[0]) {
                Comment comment = CommentMapper.INSTANCE.mapToEntity(newComment);
                commentRepository.save(newComment);
            }
            attachedComments.add(newComment);
        });
        existingComments.parallelStream().forEach(existingComment -> {
            AtomicBoolean existInNewComments = new AtomicBoolean(false);
            newComments.parallelStream().forEach(newComment -> {
                if (newComment.equals(existingComment)) {
                    existInNewComments.set(true);
                }
            });
            if (existInNewComments.getAcquire()) {
                attachedComments.add(existingComment);
            } else {

                commentRepository.save()
            }
        });

    }
}
