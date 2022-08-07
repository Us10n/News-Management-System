package by.stas.nms.exception;

/**
 * {@code NoSuchElementException} is generated in case entity not found.
 *
 * @see RuntimeException
 */
public class NoSuchElementException extends RuntimeException{

    public NoSuchElementException() {
    }

    public NoSuchElementException(String messageKey) {
        super(messageKey);
    }

    public NoSuchElementException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }

    public NoSuchElementException(Throwable cause) {
        super(cause);
    }
}
