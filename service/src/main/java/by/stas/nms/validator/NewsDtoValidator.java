package by.stas.nms.validator;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.dto.NewsDto;
import by.stas.nms.exception.ExceptionHolder;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static by.stas.nms.exception.ExceptionMessageKey.*;

@UtilityClass
public class NewsDtoValidator {
    private static final Integer MIN_STRING_LENGTH = 1;
    private static final Integer MAX_TITLE_LENGTH = 20;

    public boolean isDateValid(String date) {
        try {
            LocalDateTime.parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public boolean isTitleValid(String title) {
        return title != null && title.length() > MIN_STRING_LENGTH && title.length() <= MAX_TITLE_LENGTH;
    }

    public boolean isTextValid(String text) {
        return text != null && text.length() > MIN_STRING_LENGTH;
    }

    public void isNewsDtoValid(NewsDto newsDto, ExceptionHolder exceptionHolder) {
        if (newsDto == null) {
            exceptionHolder.addException(NULL_PASSED, CommentDto.class);
            return;
        }
        if (!isDateValid(newsDto.getDate())) {
            exceptionHolder.addException(BAD_COMMENT_DATE, newsDto.getDate());
        }
        if (!isTitleValid(newsDto.getTitle())) {
            exceptionHolder.addException(BAD_NEWS_TITLE);
        }
        if (!isTextValid(newsDto.getText())) {
            exceptionHolder.addException(BAD_COMMENT_TEXT);
        }
        newsDto.getComments().forEach(commentDto -> CommentDtoValidator.isCommentDtoValid(commentDto, exceptionHolder));

    }
}
