package by.stas.nms.validator;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.exception.ExceptionHolder;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static by.stas.nms.exception.ExceptionMessageKey.*;

@UtilityClass
public class CommentDtoValidator {


    public void isCommentDtoPayloadValid(CommentDto commentDto, ExceptionHolder exceptionHolder) {
        if (commentDto == null) {
            exceptionHolder.addException(NULL_PASSED, CommentDto.class);
            return;
        }
        if (!StringsValidator.isDateValid(commentDto.getDate().toString())) {
            exceptionHolder.addException(BAD_COMMENT_DATE, commentDto.getDate());
        }
        if (!StringsValidator.isTextValid(commentDto.getText())) {
            exceptionHolder.addException(BAD_COMMENT_TEXT, commentDto.getText());
        }
        if (!StringsValidator.isUsernameValid(commentDto.getUsername())) {
            exceptionHolder.addException(BAD_COMMENT_USERNAME, commentDto.getUsername());
        }
    }

    public void isCommentCreateDtoValid(CommentDto commentDto, ExceptionHolder exceptionHolder) {
        isCommentDtoPayloadValid(commentDto, exceptionHolder);
        StringsValidator.isIdStringValid(commentDto.getNewsId(), exceptionHolder);
    }

    public void isCommentUpdateDtoValid(CommentDto commentDto, ExceptionHolder exceptionHolder) {
        isCommentDtoPayloadValid(commentDto, exceptionHolder);
        StringsValidator.isIdStringValid(commentDto.getId(), exceptionHolder);
    }
}
