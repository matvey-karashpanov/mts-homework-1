package com.mipt.ailanakaramchakova.exception;

import com.mipt.ailanakaramchakova.dto.ErrorResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Global exception handler for all controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex,
    WebRequest request) {
    ErrorResponse error = new ErrorResponse(
      Instant.now(),
      HttpStatus.NOT_FOUND.value(),
      "NOT_FOUND",
      ex.getMessage(),
      request.getDescription(false),
      null
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, Object> details = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
      details.put(error.getField(), error.getDefaultMessage())
    );

    ErrorResponse error = new ErrorResponse(
      Instant.now(),
      HttpStatus.BAD_REQUEST.value(),
      "VALIDATION_FAILED",
      "Validation failed",
      null,
      details
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingParameter(
    MissingServletRequestParameterException ex) {
    ErrorResponse error = new ErrorResponse(
      Instant.now(),
      HttpStatus.BAD_REQUEST.value(),
      "MISSING_PARAMETER",
      ex.getMessage(),
      null,
      null
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException ex) {
    ErrorResponse error = new ErrorResponse(
      Instant.now(),
      HttpStatus.NOT_FOUND.value(),
      "ENDPOINT_NOT_FOUND",
      ex.getMessage(),
      ex.getRequestURL(),
      null
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
    logger.error("Unhandled exception", ex);

    ErrorResponse error = new ErrorResponse(
      Instant.now(),
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      "INTERNAL_ERROR",
      "An unexpected error occurred",
      request.getDescription(false),
      null
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
