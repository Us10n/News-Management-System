package by.stas.nms.exception;

/**
 * Class contains keys for exception messages.
 */
public final class ExceptionMessageKey {

    public static final String NULL_PASSED = "error.nullPassed";

    public static final String BAD_ID_STRING = "invalid.id";
    public static final String BAD_TERM_STRING = "invalid.term";

    public static final String BAD_COMMENT_DATE="invalid.comment.date";
    public static final String BAD_COMMENT_TEXT="invalid.comment.text";
    public static final String BAD_COMMENT_USERNAME="invalid.comment.username";
    public static final String EMPTY_COMMENT_PASSED="invalid.comment.empty";
    public static final String COMMENT_EXIST="invalid.comment.alreadyExist";
    public static final String COMMENT_NOT_FOUND="invalid.comment.notFound";
    public static final String COMMENT_EMPTY_LIST="invalid.comment.emptyList";

    public static final String BAD_NEWS_DATE="invalid.news.date";
    public static final String BAD_NEWS_TITLE="invalid.news.title";
    public static final String BAD_NEWS_TEXT="invalid.news.text";
    public static final String EMPTY_NEWS_PASSED="invalid.news.empty";
    public static final String NEWS_EXIST="invalid.news.alreadyExist";
    public static final String NEWS_NOT_FOUND="invalid.news.notFound";
    public static final String NEWS_EMPTY_LIST="invalid.news.emptyList";
}
