package by.stas.nms.validator;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.exception.ExceptionHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@ActiveProfiles("test")
class CommentDtoValidatorTest {

    /**
     * Payload consists of next fields: date, text, username. Payload fields are validated using StringsValidator.
     */
    @ParameterizedTest
    @MethodSource("provideValidCommentDtosPayload")
    void isCommentDtoPayloadValidPositive(CommentDto commentDto) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        CommentDtoValidator.isCommentDtoPayloadValid(commentDto, exceptionHolder);
        Assertions.assertTrue(exceptionHolder.getExceptionMessages().isEmpty());
    }

    /**
     * Payload consists of next fields: text, username. Payload fields are validated using StringsValidator.
     */
    @ParameterizedTest
    @MethodSource("provideInvalidCommentDtosPayload")
    void isCommentDtoPayloadValidNegative(CommentDto commentDto) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        CommentDtoValidator.isCommentDtoPayloadValid(commentDto, exceptionHolder);
        Assertions.assertFalse(exceptionHolder.getExceptionMessages().isEmpty());
    }

    /**
     * CreateDto consists of payload part and news_id field . CreateDto fields are validated using above tested methods.
     */
    @ParameterizedTest
    @MethodSource("provideValidCommentCreateDtos")
    void isCommentCreateDtoValidPositive(CommentDto commentDto) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        CommentDtoValidator.isCommentCreateDtoValid(commentDto, exceptionHolder);
        Assertions.assertTrue(exceptionHolder.getExceptionMessages().isEmpty());
    }

    /**
     * CreateDto consists of payload part and news_id field. CreateDto fields are validated using above tested methods.
     */
    @ParameterizedTest
    @MethodSource("provideInvalidCommentCreateDtos")
    void isCommentCreateDtoValidNegative(CommentDto commentDto) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        CommentDtoValidator.isCommentCreateDtoValid(commentDto, exceptionHolder);
        Assertions.assertFalse(exceptionHolder.getExceptionMessages().isEmpty());
    }

    /**
     * UpdateDto consists of Create Dto part, id and date fields. UpdateDto fields are validated using above tested methods.
     */
    @ParameterizedTest
    @MethodSource("provideValidCommentUpdateDtos")
    void isCommentUpdateDtoValidPositive(CommentDto commentDto) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        CommentDtoValidator.isCommentUpdateDtoValid(commentDto, exceptionHolder);
        Assertions.assertTrue(exceptionHolder.getExceptionMessages().isEmpty());
    }

    /**
     * UpdateDto consists of Create Dto part, id and date fields. UpdateDto fields are validated using above tested methods.
     */
    @ParameterizedTest
    @MethodSource("provideInvalidCommentUpdateDtos")
    void isCommentUpdateDtoValidNegative(CommentDto commentDto) {
        ExceptionHolder exceptionHolder = new ExceptionHolder();
        CommentDtoValidator.isCommentUpdateDtoValid(commentDto, exceptionHolder);
        Assertions.assertFalse(exceptionHolder.getExceptionMessages().isEmpty());
    }

    private static Stream<CommentDto> provideValidCommentDtosPayload() {
        return Stream.of(
                new CommentDto(null, null, LocalDateTime.now(), "Valid text 1", "Valid_user_name1"),
                new CommentDto(null, null, LocalDateTime.now(), "Valid text 2", "Valid_user_name2")
        );
    }

    public static Stream<CommentDto> provideInvalidCommentDtosPayload() {
        return Stream.of(
                new CommentDto(null, null, LocalDateTime.now(), " ", "Valid_user_name1"),
                new CommentDto(null, null, LocalDateTime.now(), "i", "Invalid._name"),
                new CommentDto(null, null, LocalDateTime.now(), "i", null),
                new CommentDto(null, null, LocalDateTime.now(), null, "Invalid._name"),
                new CommentDto(null, null, LocalDateTime.now(), null, null),
                null
        );
    }

    private static Stream<CommentDto> provideValidCommentCreateDtos() {
        return Stream.of(
                new CommentDto(null, "507f191e810c19729de860ea", LocalDateTime.now(), "Valid text 1", "Valid_user_name1"),
                new CommentDto(null, "507f191e810c19729de860eb", LocalDateTime.now(), "Valid text 2", "Valid_user_name2")
        );
    }

    private static Stream<CommentDto> provideInvalidCommentCreateDtos() {
        return Stream.of(
                new CommentDto(null, null, LocalDateTime.now(), " ", "Valid_user_name1"),
                new CommentDto(null, "", LocalDateTime.now(), "i", "Invalid._name"),
                new CommentDto(null, "awdawdawd", LocalDateTime.now(), "i", null),
                new CommentDto(null, "507f191e810c19729de860eb123123", LocalDateTime.now(), null, "Invalid._name"),
                new CommentDto(null, "507f191e810c19729de860e.b", LocalDateTime.now(), null, null),
                null
        );
    }

    private static Stream<CommentDto> provideValidCommentUpdateDtos() {
        return Stream.of(
                new CommentDto("507f191e810c19729de860ec", "507f191e810c19729de860ea", LocalDateTime.now(), "Valid text 1", "Valid_user_name1"),
                new CommentDto("507f191e810c19729de860ed", "507f191e810c19729de860eb", LocalDateTime.now(), "Valid text 2", "Valid_user_name2")
        );
    }

    private static Stream<CommentDto> provideInvalidCommentUpdateDtos() {
        return Stream.of(
                new CommentDto(null, null, LocalDateTime.now(), " ", "Valid_user_name1"),
                new CommentDto("", "", LocalDateTime.now(), "i", "Invalid._name"),
                new CommentDto("awdawdawd", "awdawdawd", LocalDateTime.now(), "i", null),
                new CommentDto("507f191e810c19729de860eb123123", "507f191e810c19729de860eb123123", LocalDateTime.now(), null, "Invalid._name"),
                new CommentDto("507f191e810c19729de860e.b", "507f191e810c19729de860e.b", LocalDateTime.now(), null, null),
                null
        );
    }
}