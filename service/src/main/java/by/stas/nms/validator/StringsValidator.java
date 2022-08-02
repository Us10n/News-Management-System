package by.stas.nms.validator;

import by.stas.nms.exception.ExceptionHolder;
import lombok.experimental.UtilityClass;
import org.bson.types.ObjectId;

import java.util.Objects;

import static by.stas.nms.exception.ExceptionMessageKey.BAD_ID_STRING;
import static by.stas.nms.exception.ExceptionMessageKey.BAD_TERM_STRING;

@UtilityClass
public class StringsValidator {
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
