package by.stas.nms.validator;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.exception.ExceptionHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@ActiveProfiles("test")
class NewsWithCommentsDtoValidatorTest {

    /**
     * Payload consists of next fields: title, text, comments. Payload fields are validated using StringsValidator and CommentDtoValidator.
     */
    @ParameterizedTest
    @MethodSource("provideValidNewsWithCommentsDtosPayload")
    void isNewsWithCommentsDtoPayloadValidPositive(NewsWithCommentsDto newsWithCommentsDto) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        NewsWithCommentsDtoValidator.isNewsWithCommentsDtoPayloadValid(newsWithCommentsDto,exceptionHolder);
        Assertions.assertTrue(exceptionHolder.getExceptionMessages().isEmpty());
    }

    /**
     * Payload consists of next fields: title, text, comments. Payload fields are validated using StringsValidator and CommentDtoValidator.
     */
    @ParameterizedTest
    @MethodSource("provideInvalidNewsWithCommentsDtosPayload")
    void isNewsWithCommentsDtoPayloadValidNegative(NewsWithCommentsDto newsWithCommentsDto) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        NewsWithCommentsDtoValidator.isNewsWithCommentsDtoPayloadValid(newsWithCommentsDto,exceptionHolder);
        Assertions.assertFalse(exceptionHolder.getExceptionMessages().isEmpty());
    }

    /**
     * NewsWithCommentsUpdateDto consists of NewsWithCommentsDtoPayload, id and date fields. NewsWithCommentsUpdateDto fields are validated using StringsValidator and CommentDtoValidator.
     */
    @ParameterizedTest
    @MethodSource("provideValidNewsWithCommentsUpdateDto")
    void isNewsWithCommentsUpdateDtoValidPositive(NewsWithCommentsDto newsWithCommentsDto) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        NewsWithCommentsDtoValidator.isNewsWithCommentsUpdateDtoValid(newsWithCommentsDto,exceptionHolder);
        Assertions.assertTrue(exceptionHolder.getExceptionMessages().isEmpty());
    }

    /**
     * NewsWithCommentsUpdateDto consists of NewsWithCommentsDtoPayload, id and date fields. NewsWithCommentsUpdateDto fields are validated using StringsValidator and CommentDtoValidator.
     */
    @ParameterizedTest
    @MethodSource("provideInvalidNewsWithCommentsUpdateDto")
    void isNewsWithCommentsUpdateDtoValidNegative(NewsWithCommentsDto newsWithCommentsDto) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        NewsWithCommentsDtoValidator.isNewsWithCommentsUpdateDtoValid(newsWithCommentsDto,exceptionHolder);
        Assertions.assertFalse(exceptionHolder.getExceptionMessages().isEmpty());
    }

    public static Stream<NewsWithCommentsDto> provideValidNewsWithCommentsDtosPayload() {
        CommentDto commentDto = new CommentDto(null, null, LocalDateTime.now(), "Valid text 1", "Valid_user_name1");
        return Stream.of(
                new NewsWithCommentsDto(null, LocalDateTime.now(), "Title1", "Text1", List.of(commentDto)),
                new NewsWithCommentsDto(null, LocalDateTime.now(), "Title2", "Text2", List.of(commentDto))
        );
    }

    public static Stream<NewsWithCommentsDto> provideInvalidNewsWithCommentsDtosPayload() {
        CommentDto validCommentDto = new CommentDto(null, null, LocalDateTime.now(), "Valid text 1", "Valid_user_name1");
        CommentDto invalidCommentDto = new CommentDto(null, null, LocalDateTime.now(), " ", "Valid_user_name1");
        return Stream.of(
                new NewsWithCommentsDto(null, LocalDateTime.now(), "T", "      ", List.of(validCommentDto)),
                new NewsWithCommentsDto(null, LocalDateTime.now(), "Invalid title because longer than 30 characters", "      ", List.of(validCommentDto)),
                new NewsWithCommentsDto(null, LocalDateTime.now(), "Invalid title because longer than 30 characters", "1", List.of(validCommentDto)),
                new NewsWithCommentsDto(null, LocalDateTime.now(), "Invalid title because longer than 30 characters", "  ", List.of(validCommentDto)),
                new NewsWithCommentsDto(null, LocalDateTime.now(), "Title1", "Text1", List.of(invalidCommentDto)),
                new NewsWithCommentsDto(null, LocalDateTime.now(), "Invalid title because longer than 30 characters", "  ", List.of(invalidCommentDto)),
                null
        );
    }

    public static Stream<NewsWithCommentsDto> provideValidNewsWithCommentsUpdateDto() {
        CommentDto commentDto = new CommentDto(null, null, LocalDateTime.now(), "Valid text 1", "Valid_user_name1");
        return Stream.of(
                new NewsWithCommentsDto("507f191e810c19729de860ec", LocalDateTime.now(), "Title1", "Text1", List.of(commentDto)),
                new NewsWithCommentsDto("507f191e810c19729de860ec", LocalDateTime.now(), "Title2", "Text2", List.of(commentDto))
        );
    }

    public static Stream<NewsWithCommentsDto> provideInvalidNewsWithCommentsUpdateDto() {
        CommentDto validCommentDto = new CommentDto(null, null, LocalDateTime.now(), "Valid text 1", "Valid_user_name1");
        CommentDto invalidCommentDto = new CommentDto(null, null, LocalDateTime.now(), " ", "Valid_user_name1");
        return Stream.of(
                new NewsWithCommentsDto(null, LocalDateTime.now(), "T", "      ", List.of(validCommentDto)),
                new NewsWithCommentsDto(null, LocalDateTime.now(), "Invalid title because longer than 30 characters", "      ", List.of(validCommentDto)),
                new NewsWithCommentsDto(null, LocalDateTime.now(), "Invalid title because longer than 30 characters", "1", List.of(validCommentDto)),
                new NewsWithCommentsDto(null, LocalDateTime.now(), "Invalid title because longer than 30 characters", "  ", List.of(validCommentDto)),
                new NewsWithCommentsDto(null, LocalDateTime.now(), "Title1", "Text1", List.of(invalidCommentDto)),
                new NewsWithCommentsDto(null, LocalDateTime.now(), "Invalid title because longer than 30 characters", "  ", List.of(invalidCommentDto)),
                null
        );
    }
}