package by.stas.nms.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Exception holder.
 */
public class ExceptionHolder {
    private final Map<String, Object[]> exceptionMessages;

    /**
     * Instantiates a new Exception holder.
     */
    public ExceptionHolder() {
        this.exceptionMessages = new HashMap<>();
    }

    /**
     * Add exception.
     *
     * @param messageCode the message code
     * @param args        the args
     */
    public void addException(String messageCode, Object... args) {
        exceptionMessages.put(messageCode, args);
    }

    /**
     * Gets exception messages.
     *
     * @return the exception messages
     */
    public Map<String, Object[]> getExceptionMessages() {
        return new HashMap<>(exceptionMessages);
    }
}
