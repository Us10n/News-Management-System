package by.stas.nms.validator;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.exception.ExceptionHolder;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static by.stas.nms.exception.ExceptionMessageKey.*;

@UtilityClass
public class CommentDtoValidator {
    private static final Integer MIN_TEXT_LENGTH = 1;
    /* Username length must be between 5 and 25.
       Username can have '.' , '_' and numbers, but '.' and '_' must not be neighbours or go in a row ("._" AND "_." AND ".." AND "__" is forbidden)
       Valid example: "valid.username_12" */
    private static final String VALID_USERNAME_REGEX = "^(?=[a-zA-Z0-9._]{5,25}$)(?!.*[_.]{2})[^_.].*[^_.]$";

    public boolean isDateValid(String date) {
        try {
            LocalDateTime.parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public boolean isTextValid(String text) {
        return text != null && text.length() > MIN_TEXT_LENGTH;
    }

    public boolean isUsernameValid(String username) {
        return username != null && username.matches(VALID_USERNAME_REGEX);
    }

    public void isCommentDtoValid(CommentDto commentDto, ExceptionHolder exceptionHolder) {
        if (commentDto == null) {
            exceptionHolder.addException(NULL_PASSED, CommentDto.class);
            return;
        }
        if (!isDateValid(commentDto.getDate())) {
            exceptionHolder.addException(BAD_COMMENT_DATE, commentDto.getDate());
        }
        if(!isTextValid(commentDto.getText())){
            exceptionHolder.addException(BAD_COMMENT_TEXT);
        }
        if(!isUsernameValid(commentDto.getUsername())){
            exceptionHolder.addException(BAD_COMMENT_USERNAME);
        }
    }


}
