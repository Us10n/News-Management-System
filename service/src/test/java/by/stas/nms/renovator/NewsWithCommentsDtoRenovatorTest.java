package by.stas.nms.renovator;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.entity.Comment;
import by.stas.nms.renovator.impl.NewsWithCommentsDtoRenovator;
import by.stas.nms.repository.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NewsWithCommentsDtoRenovatorTest {

    @Mock
    CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    Renovator<NewsWithCommentsDto> newsWithCommentsDtoRenovator = new NewsWithCommentsDtoRenovator(commentRepository);
    LocalDateTime currentTime = LocalDateTime.now();


    @BeforeAll
    void setUp() {
        List<Comment> comments = List.of(
                new Comment("comment1", "newsId1", currentTime, "text", "username", 0f)
        );

        Mockito.when(commentRepository.findCommentsByNewsId(Mockito.any(String.class))).thenReturn(comments);
        Mockito.when(commentRepository.saveAll(Mockito.any(Collection.class))).thenReturn(comments);
    }

    @Test
    void updateObjectWithoutNewComments() {
        List<CommentDto> commentDtoList = List.of(
                new CommentDto("comment1", "newsId1", currentTime, "text", "username")
        );
        NewsWithCommentsDto expected = new NewsWithCommentsDto("id", currentTime, "title", "text", commentDtoList);
        NewsWithCommentsDto actual = new NewsWithCommentsDto();
        actual.setId("id");
        newsWithCommentsDtoRenovator.updateObject(actual, expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateObject() {
        List<CommentDto> commentDtoList = List.of(
                new CommentDto("comment1", "newsId1", currentTime, "text", "username")
        );
        NewsWithCommentsDto expected = new NewsWithCommentsDto("id", currentTime, "title", "text", commentDtoList);
        NewsWithCommentsDto actual = new NewsWithCommentsDto(List.of());
        actual.setId("id");
        newsWithCommentsDtoRenovator.updateObject(actual, expected);
        Assertions.assertEquals(expected, actual);
    }
}
