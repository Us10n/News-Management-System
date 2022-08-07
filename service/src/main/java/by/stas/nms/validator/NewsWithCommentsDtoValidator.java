package by.stas.nms.validator;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.exception.ExceptionHolder;
import lombok.experimental.UtilityClass;

import static by.stas.nms.exception.ExceptionMessageKey.*;

/**
 * The utility class that provide methods to validate NewsWithCommentsDto
 *
 * @see StringsValidator
 * @see NewsWithCommentsDto
 */
@UtilityClass
public class NewsWithCommentsDtoValidator {

    /**
     * Validates NewsWithCommentsDto' payload. Payload consists of next fields: title, text, comments.
     *
     * @param newsWithCommentsDto      newsWithCommentsDto dto to validate
     * @param exceptionHolder holds exceptions with messages if not valid.
     */
    public void isNewsWithCommentsDtoPayloadValid(NewsWithCommentsDto newsWithCommentsDto, ExceptionHolder exceptionHolder) {
        if (newsWithCommentsDto == null) {
            exceptionHolder.addException(NULL_PASSED, CommentDto.class);
        }else {
            if (!StringsValidator.isTitleValid(newsWithCommentsDto.getTitle())) {
                exceptionHolder.addException(BAD_NEWS_TITLE, newsWithCommentsDto.getTitle());
            }
            if (!StringsValidator.isTextValid(newsWithCommentsDto.getText())) {
                exceptionHolder.addException(BAD_COMMENT_TEXT, newsWithCommentsDto.getText());
            }
            if (newsWithCommentsDto.getComments() != null) {
                newsWithCommentsDto.getComments().forEach(commentDto -> CommentDtoValidator.isCommentDtoPayloadValid(commentDto, exceptionHolder));
            }
        }
    }

    /**
     * Validates NewsWithComments' update dto. NewsWithCommentsUpdateDto consists of NewsWithCommentsDtoPayload, id and date fields.
     *
     * @param newsWithCommentsDto      newsWithCommentsDto dto to validate
     * @param exceptionHolder holds exceptions with messages if not valid.
     */
    public void isNewsWithCommentsUpdateDtoValid(NewsWithCommentsDto newsWithCommentsDto, ExceptionHolder exceptionHolder) {
        if (newsWithCommentsDto == null) {
            exceptionHolder.addException(NULL_PASSED, CommentDto.class);
        }else {
            isNewsWithCommentsDtoPayloadValid(newsWithCommentsDto, exceptionHolder);
            if (!StringsValidator.isDateValid(newsWithCommentsDto.getDate().toString())) {
                exceptionHolder.addException(BAD_COMMENT_DATE, newsWithCommentsDto.getDate());
            }
        }
    }
}
