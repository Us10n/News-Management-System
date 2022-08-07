package by.stas.nms.exception;

/**
 * {@code DuplicateEntityException} is generated in case object already exists.
 *
 * @see RuntimeException
 */
public class DuplicateEntityException extends RuntimeException{

    public DuplicateEntityException() {
    }

    public DuplicateEntityException(String messageKey) {
        super(messageKey);
    }

    public DuplicateEntityException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }

    public DuplicateEntityException(Throwable cause) {
        super(cause);
    }
}
