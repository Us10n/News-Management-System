package by.stas.nms.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {
    private static final String VERSION = " custom";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String ERROR_CODE = "errorCode";
    private static final String WRONG_ARGS_TYPE = "info.wrong.args.type";
    private static final String JSON_PARSE_ERROR = "error.json.deserialize";
    private static final String ERROR_UNEXPECTED = "error.unexpected";

    private MessageSource messageSource;

    @Autowired
    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException e, Locale locale) {
        Map<String, String> errorResponse = new HashMap<>();

        String message = messageSource.getMessage(e.getMessage(), null, locale);
        errorResponse.put(ERROR_MESSAGE, message);
        errorResponse.put(ERROR_CODE, HttpStatus.NOT_FOUND.value() + VERSION);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEntityException(DuplicateEntityException e, Locale locale) {
        Map<String, String> errorResponse = new HashMap<>();

        String message = messageSource.getMessage(e.getMessage(), null, locale);
        errorResponse.put(ERROR_MESSAGE, message);
        errorResponse.put(ERROR_CODE, HttpStatus.BAD_REQUEST.value() + VERSION);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectParameterException.class)
    public ResponseEntity<Map<String, String>> handleIncorrectParameterException(IncorrectParameterException e, Locale locale) {
        Map<String, String> errorResponse = new HashMap<>();

        Map<String, Object[]> messages = e.getExceptionHolder().getExceptionMessages();
        ArrayList<String> errorMessages = new ArrayList<>();
        messages.forEach((s, objects) -> {
            String message = messageSource.getMessage(s, objects, locale);
            errorMessages.add(message);
        });

        errorResponse.put(ERROR_MESSAGE, errorMessages.toString());
        errorResponse.put(ERROR_CODE, HttpStatus.BAD_REQUEST.value() + VERSION);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, Locale locale) {
        Map<String, String> errorResponse = new HashMap<>();
        String message = messageSource.getMessage(WRONG_ARGS_TYPE, new Object[]{ex.getName(), ex.getValue(), ex.getRequiredType()}, locale);

        errorResponse.put(ERROR_MESSAGE, message);
        errorResponse.put(ERROR_CODE, HttpStatus.BAD_REQUEST.value() + VERSION);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyListRequestedException.class)
    public ResponseEntity<Map<String, String>> handleEmptyListRequestedException(EmptyListRequestedException ex, Locale locale) {
        Map<String, String> errorResponse = new HashMap<>();
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex, Locale locale) {
        Map<String, String> errorResponse = new HashMap<>();
        String message = ex.getMessage();

        errorResponse.put(ERROR_MESSAGE, message);
        errorResponse.put(ERROR_CODE, HttpStatus.BAD_REQUEST.value() + VERSION);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, Locale locale) {
//        Map<String, String> errorResponse = new HashMap<>();
//        String message = messageSource.getMessage(JSON_PARSE_ERROR, null, locale);
//
//        errorResponse.put(ERROR_MESSAGE, message);
//        errorResponse.put(ERROR_CODE, HttpStatus.BAD_REQUEST.value() + VERSION);
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex, Locale locale) {
//        Map<String, String> errorResponse = new HashMap<>();
//
//        errorResponse.put(ERROR_MESSAGE, ex.getMessage());
//        errorResponse.put(ERROR_CODE, HttpStatus.BAD_REQUEST.value() + VERSION);
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(BadCredentialsException.class)
//    public final ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException ex, Locale locale) {
//        Map<String, String> errorResponse = new HashMap<>();
//        String message = messageSource.getMessage(USER_AUTH_EXCEPTION, null, locale);
//
//        errorResponse.put(ERROR_MESSAGE, message);
//        errorResponse.put(ERROR_CODE, HttpStatus.UNAUTHORIZED.value() + VERSION);
//        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex, Locale locale) {
        Map<String, String> errorResponse = new HashMap<>();
        String message = messageSource.getMessage(ERROR_UNEXPECTED, null, locale);

        errorResponse.put(ERROR_MESSAGE, message);
        errorResponse.put(ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value() + VERSION);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
