package by.stas.nms.exception;

/**
 * {@code EmptyObjectPassedException} is generated in case object was passed into method without initialization.
 *
 * @see RuntimeException
 */
public class EmptyObjectPassedException extends RuntimeException {

    public EmptyObjectPassedException() {
    }

    public EmptyObjectPassedException(String message) {
        super(message);
    }

    public EmptyObjectPassedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyObjectPassedException(Throwable cause) {
        super(cause);
    }
}
