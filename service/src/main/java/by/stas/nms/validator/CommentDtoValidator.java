package by.stas.nms.validator;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.exception.ExceptionHolder;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static by.stas.nms.exception.ExceptionMessageKey.*;

/**
 * The utility class that provide methods to validate CommentDto
 *
 * @see StringsValidator
 * @see CommentDto
 */
@UtilityClass
public class CommentDtoValidator {

    /**
     * Validates CommentDtos' payload. Payload consists of next fields: date, text, username.
     *
     * @param commentDto      comment dto to validate
     * @param exceptionHolder holds exceptions with messages if not valid.
     */
    public void isCommentDtoPayloadValid(CommentDto commentDto, ExceptionHolder exceptionHolder) {
        if(commentDto==null){
            exceptionHolder.addException(NULL_PASSED, CommentDto.class);
        }else {
            if (!StringsValidator.isTextValid(commentDto.getText())) {
                exceptionHolder.addException(BAD_COMMENT_TEXT, commentDto.getText());
            }
            if (!StringsValidator.isUsernameValid(commentDto.getUsername())) {
                exceptionHolder.addException(BAD_COMMENT_USERNAME, commentDto.getUsername());
            }
        }
    }

    /**
     * Validates Comments' create Dto. CreateDto consists of payload part and news_id field
     *
     * @param commentDto      comment dto to validate
     * @param exceptionHolder holds exceptions with messages if not valid.
     */
    public void isCommentCreateDtoValid(CommentDto commentDto, ExceptionHolder exceptionHolder) {
        if(commentDto==null){
            exceptionHolder.addException(NULL_PASSED, CommentDto.class);
        }else {
            isCommentDtoPayloadValid(commentDto, exceptionHolder);
            StringsValidator.isIdStringValid(commentDto.getNewsId(), exceptionHolder);
        }
    }

    /**
     * Validates Comments' update Dto. CreateDto consists of payload part, news_id field and date field.
     *
     * @param commentDto      comment dto to validate
     * @param exceptionHolder holds exceptions with messages if not valid.
     */
    public void isCommentUpdateDtoValid(CommentDto commentDto, ExceptionHolder exceptionHolder) {
        if(commentDto==null){
            exceptionHolder.addException(NULL_PASSED, CommentDto.class);
        }else {
            isCommentDtoPayloadValid(commentDto, exceptionHolder);
            if (!StringsValidator.isDateValid(commentDto.getDate().toString())) {
                exceptionHolder.addException(BAD_COMMENT_DATE, commentDto.getDate());
            }
        }
    }
}
