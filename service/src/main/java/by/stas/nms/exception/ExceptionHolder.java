package by.stas.nms.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code ExceptionHolder} holds map with exception message key and parameters.
 *
 * @see ExceptionMessageKey
 */
public class ExceptionHolder {

    private final Map<String, Object[]> exceptionMessages;

    public ExceptionHolder() {
        this.exceptionMessages = new HashMap<>();
    }

    public void addException(String messageCode, Object... args) {
        exceptionMessages.put(messageCode, args);
    }

    public Map<String, Object[]> getExceptionMessages() {
        return new HashMap<>(exceptionMessages);
    }
}
