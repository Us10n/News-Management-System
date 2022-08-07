package by.stas.nms.service.integration;

import by.stas.nms.cache.CustomCacheManager;
import by.stas.nms.config.TestConfig;
import by.stas.nms.dto.CommentDto;
import by.stas.nms.dto.NewsDto;
import by.stas.nms.entity.Comment;
import by.stas.nms.entity.News;
import by.stas.nms.exception.EmptyObjectPassedException;
import by.stas.nms.exception.IncorrectParameterException;
import by.stas.nms.exception.NoSuchElementException;
import by.stas.nms.renovator.Renovator;
import by.stas.nms.repository.CommentRepository;
import by.stas.nms.repository.NewsRepository;
import by.stas.nms.service.CommentService;
import by.stas.nms.service.impl.CommentServiceImpl;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Integration testing CommentService, embedded MongoDb and embedded Hazelcast
 */
@DataMongoTest
@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentServiceImplTest {

    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    Renovator<CommentDto> commentDtoRenovator;
    @Autowired
    CustomCacheManager customCacheManager;

    static final String NEWS_ID = "62ecdb0adbeacf6ffbd7d7a1";
    static final String COMMENT_ID = "ada1291e811219729de860c%d";
    static final String TEXT = "Text %d";

    @BeforeAll
    void setup() {
        fillDatabase();
        commentService = new CommentServiceImpl(commentRepository, newsRepository, commentDtoRenovator, customCacheManager);
    }

    @Transactional
    void fillDatabase() {
        News news = new News(NEWS_ID, LocalDateTime.now(), "Title", "Text", 0f);
        newsRepository.save(news);
        String newsId = news.getId();
        List<Comment> commentList = List.of(
                new Comment(String.format(COMMENT_ID, 1), newsId, LocalDateTime.now(), String.format(TEXT, 1), "User_1", 0f),
                new Comment(String.format(COMMENT_ID, 2), newsId, LocalDateTime.now(), String.format(TEXT, 2), "User_1", 0f),
                new Comment(String.format(COMMENT_ID, 3), newsId, LocalDateTime.now(), String.format(TEXT, 3), "User_2", 0f),
                new Comment(String.format(COMMENT_ID, 4), newsId, LocalDateTime.now(), String.format(TEXT, 4), "User_3", 0f)
        );
        commentRepository.saveAll(commentList);
    }

    @Test
    void create() {
        CommentDto expected = new CommentDto(null, NEWS_ID, LocalDateTime.now(), String.format(TEXT, 5), "User_4");
        CommentDto actual = commentService.create(expected);
        Assertions.assertNotNull(actual.getId());
    }

    @Test
    void createNull() {
        Assertions.assertThrows(
                IncorrectParameterException.class,
                () -> commentService.create(null)
        );
    }

    @Test
    void createInvalid() {
        CommentDto expected = new CommentDto(null, "invalid id", LocalDateTime.now(), "C", "User_.4");
        Assertions.assertThrows(
                IncorrectParameterException.class,
                () -> commentService.create(expected)
        );
    }

    @Test
    void readAll() {
        List<CommentDto> commentDtos = commentService.readAll(0, 10);
        boolean actual = commentDtos.stream().anyMatch(commentDto -> commentDto.getId().equals(String.format(COMMENT_ID, 1)));
        Assertions.assertTrue(actual);
    }

    @Test
    void readAllByTerm() {
        List<CommentDto> commentDtos = commentService.readAll("User_1", 0, 10);
        boolean actual = commentDtos.stream().anyMatch(commentDto -> commentDto.getId().equals(String.format(COMMENT_ID, 1)));
        Assertions.assertTrue(actual);
    }

    @Test
    void readAllEmptyPage() {
        List<CommentDto> actual = commentService.readAll(200, 10);
        List<CommentDto> expected = new ArrayList<>();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById() {
        CommentDto readComent = commentService.readById(String.format(COMMENT_ID, 2));
        String expected = String.format(TEXT, 2);
        String actual = readComent.getText();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readByIdInvalidKey() {
        Assertions.assertThrows(
                IncorrectParameterException.class,
                () -> commentService.readById(null)
        );
    }

    @Test
    void update() {
        CommentDto commentDto = new CommentDto(null, NEWS_ID, LocalDateTime.now(), " updated Comment text2", "User_1");
        CommentDto updatedCommentDto = commentService.update(String.format(COMMENT_ID, 2), commentDto);
        boolean actual = updatedCommentDto.getText().equals(commentDto.getText());
        Assertions.assertTrue(actual);
    }

    @Test
    void updateNullId() {
        CommentDto object = new CommentDto(null, NEWS_ID, LocalDateTime.now(), " updated Comment text2", "User_1");
        Assertions.assertThrows(
                IncorrectParameterException.class,
                () -> commentService.update(null, object)
        );
    }

    @Test
    void updateNullObject() {
        Assertions.assertThrows(
                EmptyObjectPassedException.class,
                () -> commentService.update(null, null)
        );
    }

    @Test
    void updateInvalidObject() {
        CommentDto commentDto = new CommentDto(String.format(COMMENT_ID, 2), "invalid id", LocalDateTime.now(), "     ", "User_.1");
        Assertions.assertThrows(
                IncorrectParameterException.class,
                () -> commentService.update(String.format(COMMENT_ID, 2), commentDto)
        );
    }

    @Test
    void delete() {
        commentService.delete(String.format(COMMENT_ID, 4));
        Optional<Comment> comment = commentRepository.findCommentById(String.format(COMMENT_ID, 4));
        Assertions.assertEquals(Optional.empty(), comment);
    }

    @Test
    void deleteInvalid() {
        Assertions.assertThrows(
                IncorrectParameterException.class,
                () -> commentService.delete("invalid id")
        );
    }

    @Test
    void deleteNotFound() {
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> commentService.delete("ada1291e811219729de860d1")
        );
    }
}