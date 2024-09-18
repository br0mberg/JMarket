package ru.brombin.JMarket.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.brombin.JMarket.util.exceptions.NotCreatedOrUpdatedException;
import ru.brombin.JMarket.util.exceptions.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e,  WebRequest request) {
        logger.warn("NotFoundException occurred: {} - Path: {}", e.getMessage(), request.getDescription(false));
        return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotCreatedOrUpdatedException.class)
    public ResponseEntity<ErrorResponse> handleNotCreatedOrUpdatedException(NotCreatedOrUpdatedException e,  WebRequest request) {
        logger.warn("NotCreatedOrUpdatedException occurred: {} - Path: {}", e.getMessage(), request.getDescription(false));
        return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception e, WebRequest request) {
        logger.error("An unexpected error occurred: {} - Path: {}", e.getMessage(), request.getDescription(false), e);
        ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred", System.currentTimeMillis());
        return buildErrorResponse(errorResponse.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(message, System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, status);
    }
}
