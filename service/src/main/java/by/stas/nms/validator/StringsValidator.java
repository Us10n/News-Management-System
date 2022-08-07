package by.stas.nms.validator;

import by.stas.nms.exception.ExceptionHolder;
import lombok.experimental.UtilityClass;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import static by.stas.nms.exception.ExceptionMessageKey.BAD_ID_STRING;
import static by.stas.nms.exception.ExceptionMessageKey.BAD_TERM_STRING;

/**
 * The utility class that provide methods to validate Strings. Used to validate fields in CommentDtoValidator and NewsWithCommentsDtoValidator.
 *
 * @see CommentDtoValidator
 * @see NewsWithCommentsDtoValidator
 */
@UtilityClass
public class StringsValidator {
    private static final Integer MIN_TEXT_LENGTH = 1;
    private static final Integer MIN_TITLE_LENGTH = 1;
    private static final Integer MAX_TITLE_LENGTH = 30;
    /* Username length must be between 5 and 25. Latin characters only.
       Username can have '.' , '_' and numbers, but '.' and '_' must not be neighbours or go in a row ("._" AND "_." AND ".." AND "__" is forbidden)
       Valid example: "valid.username_12" */
    private static final String VALID_USERNAME_REGEX = "^(?=[a-zA-Z0-9._]{5,25}$)(?!.*[_.]{2})[^_.].*[^_.]$";

    public boolean isDateValid(String date) {
        if (date == null) {
            return false;
        }
        try {
            LocalDateTime.parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public boolean isTitleValid(String title) {
        return title != null && title.length() > MIN_TITLE_LENGTH && title.length() <= MAX_TITLE_LENGTH;
    }

    public boolean isTextValid(String text) {
        return text != null && text.length() > MIN_TEXT_LENGTH;
    }

    public boolean isUsernameValid(String username) {
        return username != null && username.matches(VALID_USERNAME_REGEX);
    }

    public void isIdStringValid(String id, ExceptionHolder exceptionHolder) {
        if (Objects.isNull(id) || !ObjectId.isValid(id)) {
            exceptionHolder.addException(BAD_ID_STRING, id);
        }
    }

    public void isTermStringValid(String term, ExceptionHolder exceptionHolder) {
        if (Objects.isNull(term) || term.isBlank()) {
            exceptionHolder.addException(BAD_TERM_STRING, term);
        }
    }
}
