package by.stas.nms.validator;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.exception.ExceptionHolder;
import lombok.experimental.UtilityClass;

import static by.stas.nms.exception.ExceptionMessageKey.*;

@UtilityClass
public class NewsWithCommentsDtoValidator {

    public void isNewsWithCommentsDtoPayloadValid(NewsWithCommentsDto newsDto, ExceptionHolder exceptionHolder) {
        if (newsDto == null) {
            exceptionHolder.addException(NULL_PASSED, CommentDto.class);
            return;
        }
        if (!StringsValidator.isDateValid(newsDto.getDate().toString())) {
            exceptionHolder.addException(BAD_COMMENT_DATE, newsDto.getDate());
        }
        if (!StringsValidator.isTitleValid(newsDto.getTitle())) {
            exceptionHolder.addException(BAD_NEWS_TITLE, newsDto.getTitle());
        }
        if (!StringsValidator.isTextValid(newsDto.getText())) {
            exceptionHolder.addException(BAD_COMMENT_TEXT, newsDto.getText());
        }
        if (newsDto.getComments() != null) {
            newsDto.getComments().forEach(commentDto -> CommentDtoValidator.isCommentDtoPayloadValid(commentDto, exceptionHolder));
        }
    }

    public void isNewsWithCommentsUpdateDtoValid(NewsWithCommentsDto newsDto, ExceptionHolder exceptionHolder) {
        isNewsWithCommentsDtoPayloadValid(newsDto, exceptionHolder);
        StringsValidator.isIdStringValid(newsDto.getId(), exceptionHolder);
    }
}
