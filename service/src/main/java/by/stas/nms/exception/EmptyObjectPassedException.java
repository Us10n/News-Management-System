package by.stas.nms.exception;

public class EmptyObjectPassedException extends RuntimeException {
    public EmptyObjectPassedException() {
        super();
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
