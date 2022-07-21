package by.stas.nms.exception;

/**
 * The type Duplicate entity exception.
 */
public class DuplicateEntityException extends RuntimeException{
    /**
     * Instantiates a new Duplicate entity exception.
     */
    public DuplicateEntityException() {
    }

    /**
     * Instantiates a new Duplicate entity exception.
     *
     * @param messageKey the message key
     */
    public DuplicateEntityException(String messageKey) {
        super(messageKey);
    }

    /**
     * Instantiates a new Duplicate entity exception.
     *
     * @param messageKey the message key
     * @param cause      the cause
     */
    public DuplicateEntityException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }

    /**
     * Instantiates a new Duplicate entity exception.
     *
     * @param cause the cause
     */
    public DuplicateEntityException(Throwable cause) {
        super(cause);
    }
}
