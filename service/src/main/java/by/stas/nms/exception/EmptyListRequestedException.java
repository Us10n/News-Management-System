package by.stas.nms.exception;

public class EmptyListRequestedException extends RuntimeException{
    public EmptyListRequestedException() {
        super();
    }

    public EmptyListRequestedException(String message) {
        super(message);
    }

    public EmptyListRequestedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyListRequestedException(Throwable cause) {
        super(cause);
    }
}
