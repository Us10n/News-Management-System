package by.stas.nms.exception;

/**
 * The type No such element exception.
 */
public class NoSuchElementException extends RuntimeException{
    /**
     * Instantiates a new No such element exception.
     */
    public NoSuchElementException() {
    }

    /**
     * Instantiates a new No such element exception.
     *
     * @param messageKey the message key
     */
    public NoSuchElementException(String messageKey) {
        super(messageKey);
    }

    /**
     * Instantiates a new No such element exception.
     *
     * @param messageKey the message key
     * @param cause      the cause
     */
    public NoSuchElementException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }

    /**
     * Instantiates a new No such element exception.
     *
     * @param cause the cause
     */
    public NoSuchElementException(Throwable cause) {
        super(cause);
    }
}
