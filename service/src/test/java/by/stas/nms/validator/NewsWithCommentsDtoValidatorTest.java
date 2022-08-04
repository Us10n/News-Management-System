package by.stas.nms.validator;

import org.junit.jupiter.api.Test;

class NewsWithCommentsDtoValidatorTest {

    /**
     * Payload consists of next fields: date, title, text, comments. Payload fields are validated using StringsValidator and CommentDtoValidator.
     */
    @Test
    void isNewsWithCommentsDtoPayloadValid() {
    }

    /**
     * NewsWithCommentsUpdateDto consists of NewsWithCommentsDtoPayload and id. NewsWithCommentsUpdateDto fields are validated using StringsValidator and CommentDtoValidator.
     */
    @Test
    void isNewsWithCommentsUpdateDtoValid() {
    }
}