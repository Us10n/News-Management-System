package by.stas.nms.service.integration;

import by.stas.nms.cache.CustomCacheManager;
import by.stas.nms.config.TestConfig;
import by.stas.nms.dto.NewsDto;
import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.entity.Comment;
import by.stas.nms.entity.News;
import by.stas.nms.exception.EmptyObjectPassedException;
import by.stas.nms.exception.IncorrectParameterException;
import by.stas.nms.exception.NoSuchElementException;
import by.stas.nms.renovator.Renovator;
import by.stas.nms.repository.CommentRepository;
import by.stas.nms.repository.NewsRepository;
import by.stas.nms.service.NewsService;
import by.stas.nms.service.impl.NewsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Integration testing NewsService, embedded MongoDb and embedded Hazelcast
 */
@DataMongoTest
@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NewsServiceImplTest {

    NewsService newsService;

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    Renovator<NewsWithCommentsDto> newsWithCommentsDtoRenovator;
    @Autowired
    CustomCacheManager customCacheManager;

    static final String NEWS_ID = "62ecdb0adbeacf6ffbd7d7a%d";
    static final String COMMENT_ID = "ada1291e811219729de860c%d";
    static final String TITLE = "Title %d";
    static final String TEXT = "Text %d";

    @BeforeAll
    void setUp() {
        fillDatabase();
        newsService = new NewsServiceImpl(newsRepository, commentRepository, newsWithCommentsDtoRenovator, customCacheManager);
    }

    void fillDatabase() {
        for (int i = 1; i < 6; i++) {
            News news = new News(String.format(NEWS_ID, i), LocalDateTime.now(), String.format(TITLE, i), String.format(TEXT, i), 0f);
            newsRepository.save(news);
            String newsId = news.getId();
            List<Comment> commentList = List.of(
                    new Comment(String.format(COMMENT_ID, 1), newsId, LocalDateTime.now(), "Comment text1", "User_1", 0f),
                    new Comment(String.format(COMMENT_ID, 2), newsId, LocalDateTime.now(), "Comment text2", "User_1", 0f),
                    new Comment(String.format(COMMENT_ID, 3), newsId, LocalDateTime.now(), "Comment text3", "User_2", 0f)
            );
            commentRepository.saveAll(commentList);
        }
    }

    @Test
    void create() {
        NewsWithCommentsDto actual = newsService.create(new NewsWithCommentsDto(null, null, "title321", "Text123", null));
        Assertions.assertNotNull(actual.getId());
    }

    @Test
    void createNull() {
        Assertions.assertThrows(
                IncorrectParameterException.class,
                () -> newsService.create(null)
        );
    }

    @Test
    void createInvalid() {
        Assertions.assertThrows(
                IncorrectParameterException.class,
                () -> newsService.create(new NewsWithCommentsDto(null, null, "q", "    ", null))
        );
    }

    @Test
    void readAll() {
        List<NewsDto> newsDtos = newsService.readAll(0, 10);
        boolean actual = newsDtos.stream().anyMatch(newsDto -> newsDto.getId().equals(String.format(NEWS_ID, 2)));
        Assertions.assertTrue(actual);
    }

    @Test
    void readAllByTerm() {
        List<NewsDto> newsDtos = newsService.readAll(String.format(TEXT, 3),0, 10);
        boolean actual = newsDtos.stream().anyMatch(newsDto -> newsDto.getId().equals(String.format(NEWS_ID, 3)));
        Assertions.assertTrue(actual);
    }

    @Test
    void readAllEmptyPage() {
        List<NewsDto> actual = newsService.readAll(200, 10);
        List<NewsDto> expected = new ArrayList<>();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById() {
        NewsWithCommentsDto found = newsService.readById(String.format(NEWS_ID, 3), 0, 10);
        String expected = String.format(TEXT, 3);
        String actual = found.getText();
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void readByIdNoComments() {
        NewsWithCommentsDto newsWithCommentsDto = newsService.readById(String.format(NEWS_ID, 3), 120, 10);
        int size = newsWithCommentsDto.getComments().size();
        Assertions.assertEquals(0, size);
    }

    @Test
    void readByIdNotFound() {
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> newsService.readById("ada1291e811219729de860c1", 0, 10)
        );
    }

    @Test
    void update() {
        NewsWithCommentsDto expected = new NewsWithCommentsDto(null, LocalDateTime.now(), "title321", "updated Text123", null);
        NewsWithCommentsDto actual = newsService.update(String.format(NEWS_ID, 4), expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateNullId() {
        NewsWithCommentsDto object = new NewsWithCommentsDto(null, LocalDateTime.now(), "title321", "updated Text123", null);
        Assertions.assertThrows(
                IncorrectParameterException.class,
                () -> newsService.update(null, object)
        );
    }

    @Test
    void updateNullObject() {
        Assertions.assertThrows(
                EmptyObjectPassedException.class,
                () -> newsService.update(null, null)
        );
    }

    @Test
    void updateInvalidObject() {
        NewsWithCommentsDto newsWithCommentsDto = new NewsWithCommentsDto(null, null, "q", "    ", null);
        Assertions.assertThrows(
                IncorrectParameterException.class,
                () -> newsService.update(String.format(NEWS_ID, 4), newsWithCommentsDto)
        );
    }

    @Test
    void delete() {
        newsService.delete(String.format(NEWS_ID, 1));
        Optional<News> news = newsRepository.findNewsById(String.format(NEWS_ID, 1));
        Assertions.assertEquals(Optional.empty(), news);
    }

    @Test
    void deleteInvalid() {
        Assertions.assertThrows(
                IncorrectParameterException.class,
                () -> newsService.delete("invalid id")
        );
    }

    @Test
    void deleteNotFound() {
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> newsService.delete("ada1291e811219729de860d1")
        );
    }
}