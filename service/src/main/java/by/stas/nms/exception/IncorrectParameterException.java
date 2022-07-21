package by.stas.nms.exception;

/**
 * The type Incorrect parameter exception.
 */
public class IncorrectParameterException extends RuntimeException {
    private final ExceptionHolder exceptionHolder;

    /**
     * Instantiates a new Incorrect parameter exception.
     *
     * @param exceptionHolder the exception holder
     */
    public IncorrectParameterException(ExceptionHolder exceptionHolder) {
        this.exceptionHolder = exceptionHolder;
    }

    /**
     * Gets exception holder.
     *
     * @return the exception holder
     */
    public ExceptionHolder getExceptionHolder() {
        return exceptionHolder;
    }
}
