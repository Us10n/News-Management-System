package by.stas.nms.exception;

/**
 * {@code IncorrectParameterException} is generated in case received parameters have unacceptable value.
 *
 * @see RuntimeException
 */
public class IncorrectParameterException extends RuntimeException {

    private final ExceptionHolder exceptionHolder;

    public IncorrectParameterException(ExceptionHolder exceptionHolder) {
        this.exceptionHolder = exceptionHolder;
    }

    public ExceptionHolder getExceptionHolder() {
        return exceptionHolder;
    }
}
