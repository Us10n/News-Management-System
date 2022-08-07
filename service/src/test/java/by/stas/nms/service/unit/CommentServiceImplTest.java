package by.stas.nms.service.unit;

import by.stas.nms.cache.CustomCacheManager;
import by.stas.nms.dto.CommentDto;
import by.stas.nms.entity.Comment;
import by.stas.nms.entity.News;
import by.stas.nms.exception.EmptyObjectPassedException;
import by.stas.nms.exception.IncorrectParameterException;
import by.stas.nms.exception.NoSuchElementException;
import by.stas.nms.renovator.Renovator;
import by.stas.nms.renovator.impl.CommentDtoRenovator;
import by.stas.nms.repository.CommentRepository;
import by.stas.nms.repository.NewsRepository;
import by.stas.nms.service.CommentService;
import by.stas.nms.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentServiceImplTest {

    @Mock
    CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    @Mock
    NewsRepository newsRepository = Mockito.mock(NewsRepository.class);
    @Mock
    CustomCacheManager customCacheManager = Mockito.mock(CustomCacheManager.class);

    CommentService commentService;
    Renovator<CommentDto> commentDtoRenovator = new CommentDtoRenovator();

    static final String NEWS_ID = "62ecdb0adbeacf6ffbd7d7a1";
    static final String COMMENT_ID = "ada1291e811219729de860c%d";
    static final String TEXT = "Text %d";

    @BeforeAll
    void setup() {
        News news = new News(NEWS_ID, LocalDateTime.now(), "Title", "Text", 0f);
        List<Comment> comments = List.of(
                new Comment(String.format(COMMENT_ID, 1), NEWS_ID, LocalDateTime.now(), String.format(TEXT, 1), "User_1", 0f),
                new Comment(String.format(COMMENT_ID, 2), NEWS_ID, LocalDateTime.now(), String.format(TEXT, 2), "User_1", 0f),
                new Comment(String.format(COMMENT_ID, 3), NEWS_ID, LocalDateTime.now(), String.format(TEXT, 3), "User_2", 0f),
                new Comment(String.format(COMMENT_ID, 4), NEWS_ID, LocalDateTime.now(), String.format(TEXT, 4), "User_3", 0f)
        );

        Mockito.when(newsRepository.findNewsById(NEWS_ID)).thenReturn(Optional.of(news));
        Mockito.when(commentRepository.findCommentsByNewsId(NEWS_ID)).thenReturn(comments);
        Mockito.when(commentRepository.findCommentById(String.format(COMMENT_ID, 5))).thenReturn(Optional.empty());
        Mockito.when(commentRepository.findCommentById(String.format(COMMENT_ID, 4))).thenReturn(Optional.of(comments.get(3)));
        Mockito.when(commentRepository.findCommentById(String.format(COMMENT_ID, 2))).thenReturn(Optional.of(comments.get(1)));
        Comment createdAndUpdatedComment = new Comment(String.format(COMMENT_ID, 5), NEWS_ID, LocalDateTime.now(), " updated Comment text2", "User_4", 0f);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(createdAndUpdatedComment);
        Mockito.when(commentRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(comments));
        Mockito.when(commentRepository.findAll(PageRequest.of(200, 10))).thenReturn(new PageImpl<>(List.of()));
        Mockito.when(commentRepository.findAllBy(
                TextCriteria.forDefaultLanguage().matching("User_1"),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "score"))
        )).thenReturn(List.of(comments.get(0)));


        commentService = new CommentServiceImpl(commentRepository, newsRepository, commentDtoRenovator, customCacheManager);
    }

    @Test
    void create() {
        CommentDto expected = new CommentDto(null, NEWS_ID, LocalDateTime.now(), " updated Comment text2", "User_4");
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