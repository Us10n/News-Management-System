package by.stas.nms.service.impl;

import by.stas.nms.cache.CustomCacheManager;
import by.stas.nms.dto.CommentDto;
import by.stas.nms.dto.NewsDto;
import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.entity.Comment;
import by.stas.nms.entity.News;
import by.stas.nms.exception.EmptyObjectPassedException;
import by.stas.nms.exception.ExceptionHolder;
import by.stas.nms.exception.IncorrectParameterException;
import by.stas.nms.exception.NoSuchElementException;
import by.stas.nms.mapper.CommentMapper;
import by.stas.nms.mapper.NewsMapper;
import by.stas.nms.mapper.NewsWithCommentsMapper;
import by.stas.nms.renovator.Renovator;
import by.stas.nms.repository.CommentRepository;
import by.stas.nms.repository.NewsRepository;
import by.stas.nms.service.NewsService;
import by.stas.nms.validator.NewsWithCommentsDtoValidator;
import by.stas.nms.validator.StringsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static by.stas.nms.exception.ExceptionMessageKey.EMPTY_NEWS_PASSED;
import static by.stas.nms.exception.ExceptionMessageKey.NEWS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final static String NEWS_CACHE_NAME = "news";
    private final static String COMMENTS_CACHE_NAME = "comments";

    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final Renovator<NewsWithCommentsDto> newsWithCommentsDtoRenovator;
    private final CustomCacheManager cacheManager;

    @Override
    @Transactional(value = "mongoTransactionManager", propagation = Propagation.REQUIRED)
    public NewsWithCommentsDto create(NewsWithCommentsDto object) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        NewsWithCommentsDtoValidator.isNewsWithCommentsDtoPayloadValid(object, exceptionHolder);
        if (!exceptionHolder.getExceptionMessages().isEmpty()) {
            throw new IncorrectParameterException(exceptionHolder);
        }

        LocalDateTime createDate = LocalDateTime.now();
        News newsToCreate = NewsWithCommentsMapper.INSTANCE.mapToEntity(object);
        //Set create date to news.
        //In order to create new document with auto-generated _id field id field must be null.
        newsToCreate.setId(null);
        newsToCreate.setDate(createDate);
        News createdNews = newsRepository.save(newsToCreate);

        //Set create date to comments.
        //In order to create new document with auto-generated _id field id field must be null.
        List<Comment> createdComments = new ArrayList<>();
        if (Objects.nonNull(object.getComments())) {
            List<Comment> comments = object.getComments()
                    .stream()
                    .map(commentDto -> {
                        commentDto.setId(null);
                        commentDto.setDate(createDate);
                        commentDto.setNewsId(createdNews.getId());
                        return CommentMapper.INSTANCE.mapToEntity(commentDto);
                    })
                    .toList();
            createdComments = commentRepository.saveAll(comments);
        }

        //Invalidate cache due to objects creation
        cacheManager.invalidateCacheMap(COMMENTS_CACHE_NAME);
        cacheManager.invalidateCacheMap(NEWS_CACHE_NAME);

        NewsWithCommentsDto createdNewsWithCommentDto = NewsWithCommentsMapper.INSTANCE.mapToDto(createdNews);
        List<CommentDto> createdCommentDtos = createdComments
                .stream()
                .map(CommentMapper.INSTANCE::mapToDto).toList();
        createdNewsWithCommentDto.setComments(createdCommentDtos);

        return createdNewsWithCommentDto;
    }

    @Override
    public List<NewsDto> readAll(Integer page, Integer limit) {
        String key = cacheManager.generateKeyForCacheMap(page, limit);

        Object[] cachedNews = cacheManager.getCollectionFromCacheMap(NEWS_CACHE_NAME, key);
        List<NewsDto> newsDtos;
        if (Objects.nonNull(cachedNews)) {
            newsDtos = Arrays.stream(cachedNews)
                    .map(object -> (NewsDto) object)
                    .toList();
        } else {
            PageRequest pageRequest = PageRequest.of(page, limit);
            newsDtos = newsRepository.findAll(pageRequest)
                    .getContent()
                    .stream()
                    .map(NewsMapper.INSTANCE::mapToDto)
                    .toList();

            cacheManager.putCollectionToCacheMap(NEWS_CACHE_NAME, key, newsDtos.toArray());
        }

        return newsDtos;
    }

    @Override
    public List<NewsDto> readAll(String term, Integer page, Integer limit) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        StringsValidator.isTermStringValid(term, exceptionHolder);
        if (!exceptionHolder.getExceptionMessages().isEmpty()) {
            throw new IncorrectParameterException(exceptionHolder);
        }

        String key = cacheManager.generateKeyForCacheMap(term, page, limit);

        Object[] cachedNews = cacheManager.getCollectionFromCacheMap(NEWS_CACHE_NAME, key);
        List<NewsDto> newsDtos;
        if (Objects.nonNull(cachedNews)) {
            newsDtos = Arrays.stream(cachedNews)
                    .map(object -> (NewsDto) object)
                    .toList();
        } else {
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "score"));
            TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(term);
            newsDtos = newsRepository.findAllBy(criteria, pageRequest)
                    .stream()
                    .map(NewsMapper.INSTANCE::mapToDto)
                    .toList();

            cacheManager.putCollectionToCacheMap(NEWS_CACHE_NAME, key, newsDtos.toArray());
        }

        return newsDtos;
    }

    @Override
    public NewsWithCommentsDto readById(String id, Integer page, Integer limit) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        StringsValidator.isIdStringValid(id, exceptionHolder);
        if (!exceptionHolder.getExceptionMessages().isEmpty()) {
            throw new IncorrectParameterException(exceptionHolder);
        }

        String key = cacheManager.generateKeyForCacheMap(id, page, limit);

        Optional<Object> cachedNews = cacheManager.getObjectFromCacheMap(NEWS_CACHE_NAME, key);
        NewsWithCommentsDto newsWithCommentsDto;
        if (cachedNews.isPresent()) {
            newsWithCommentsDto = (NewsWithCommentsDto) cachedNews.get();
        } else {
            Optional<News> optionalNews = newsRepository.findNewsById(id);
            News foundNews = optionalNews.orElseThrow(
                    () -> new NoSuchElementException(NEWS_NOT_FOUND)
            );

            PageRequest pageRequest = PageRequest.of(page, limit);
            List<Comment> foundComments = commentRepository.findCommentsByNewsId(pageRequest, id).getContent();

            newsWithCommentsDto = NewsWithCommentsMapper.INSTANCE.mapToDto(foundNews);
            List<CommentDto> commentDtos = foundComments.stream().map(CommentMapper.INSTANCE::mapToDto).toList();
            newsWithCommentsDto.setComments(commentDtos);

            cacheManager.putObjectToCacheMap(NEWS_CACHE_NAME, key, newsWithCommentsDto);
        }

        return newsWithCommentsDto;
    }

    @Override
    public NewsWithCommentsDto readById(String id) {
        return readById(id, 0, 10);
    }

    @Override
    public NewsDto readByIdWithoutComments(String id) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        StringsValidator.isIdStringValid(id, exceptionHolder);
        if (!exceptionHolder.getExceptionMessages().isEmpty()) {
            throw new IncorrectParameterException(exceptionHolder);
        }

        String key = cacheManager.generateKeyForCacheMap(id);

        Optional<Object> cachedNews = cacheManager.getObjectFromCacheMap(NEWS_CACHE_NAME, key);
        NewsDto newsDto;
        if (cachedNews.isPresent()) {
            newsDto = (NewsDto) cachedNews.get();
        } else {
            Optional<News> optionalNews = newsRepository.findNewsById(id);
            News foundNews = optionalNews.orElseThrow(
                    () -> new NoSuchElementException(NEWS_NOT_FOUND)
            );

            newsDto = NewsMapper.INSTANCE.mapToDto(foundNews);

            cacheManager.putObjectToCacheMap(NEWS_CACHE_NAME, key, newsDto);
        }

        return newsDto;
    }

    @Override
    @Transactional(value = "mongoTransactionManager", propagation = Propagation.REQUIRED)
    public NewsWithCommentsDto update(String id, NewsWithCommentsDto object) {
        if (!isUpdateObjectContainNewValues(object)) {
            throw new EmptyObjectPassedException(EMPTY_NEWS_PASSED);
        }

        NewsDto existingNewsDto = readByIdWithoutComments(id);
        object.setId(id);
        newsWithCommentsDtoRenovator.updateObject(object, new NewsWithCommentsDto(existingNewsDto));

        ExceptionHolder exceptionHolder = new ExceptionHolder();
        NewsWithCommentsDtoValidator.isNewsWithCommentsUpdateDtoValid(object, exceptionHolder);
        if (!exceptionHolder.getExceptionMessages().isEmpty()) {
            throw new IncorrectParameterException(exceptionHolder);
        }

        News newsToUpdate = NewsWithCommentsMapper.INSTANCE.mapToEntity(object);
        News updatedNews = newsRepository.save(newsToUpdate);

        //Invalidate cache due to object update
        cacheManager.invalidateCacheMap(NEWS_CACHE_NAME);

        NewsWithCommentsDto updatedNewsWithCommentsDto = NewsWithCommentsMapper.INSTANCE.mapToDto(updatedNews);
        updatedNewsWithCommentsDto.setComments(object.getComments());

        return updatedNewsWithCommentsDto;
    }

    @Override
    @Transactional(value = "mongoTransactionManager", propagation = Propagation.REQUIRED)
    public void delete(String id) {
        //Check whether news with this id exist or not
        readByIdWithoutComments(id);

        commentRepository.deleteCommentsByNewsId(id);
        newsRepository.deleteById(id);

        //Invalidate cache due to objects removing
        cacheManager.invalidateCacheMap(COMMENTS_CACHE_NAME);
        cacheManager.invalidateCacheMap(NEWS_CACHE_NAME);
    }

    private boolean isUpdateObjectContainNewValues(NewsWithCommentsDto object) {
        return Objects.nonNull(object) && (
                Objects.nonNull(object.getComments())
                        || Objects.nonNull(object.getText())
                        || Objects.nonNull(object.getTitle()));
    }
}
