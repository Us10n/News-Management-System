package by.stas.nms.service.integration;

import by.stas.nms.cache.CustomCacheManager;
import by.stas.nms.config.TestConfig;
import by.stas.nms.dto.CommentDto;
import by.stas.nms.entity.Comment;
import by.stas.nms.entity.News;
import by.stas.nms.exception.EmptyListRequestedException;
import by.stas.nms.exception.IncorrectParameterException;
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
import java.util.List;

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

    @BeforeAll
    void setup() {
        fillDatabase();
        commentService = new CommentServiceImpl(commentRepository, newsRepository, commentDtoRenovator, customCacheManager);
    }

    @Transactional
    void fillDatabase() {
        News news = new News("news1", LocalDateTime.now(), "Title", "Text", 0f);
        newsRepository.save(news);
        List<Comment> commentList = List.of(
                new Comment("comment1", news.getId(), LocalDateTime.now(), "Comment text1", "User_1", 0f),
                new Comment("comment2", news.getId(), LocalDateTime.now(), "Comment text2", "User_1", 0f),
                new Comment("comment3", news.getId(), LocalDateTime.now(), "Comment text3", "User_2", 0f),
                new Comment("comment4", news.getId(), LocalDateTime.now(), "Comment text4", "User_3", 0f)
        );
        commentRepository.saveAll(commentList);
    }

    @Test
    void create() {
//        System.out.println("hi");
    }

    @Test
    void readAll() {
        List<CommentDto> commentDtos = commentService.readAll(0, 10);
        boolean actual = commentDtos.stream().anyMatch(commentDto -> commentDto.getId().equals("comment1"));
        Assertions.assertTrue(actual);
    }

    @Test
    void readAllEmptyPage() {
        Assertions.assertThrows(
                EmptyListRequestedException.class,
                () -> commentService.readAll(11, 10)
        );
    }

    @Test
    void readById() {
        CommentDto actual = commentService.readById("comment2");
        CommentDto expected = new CommentDto("comment2", "new1", LocalDateTime.now(), "Comment text2", "User_1");
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
    }

    @Test
    void delete() {
    }
}