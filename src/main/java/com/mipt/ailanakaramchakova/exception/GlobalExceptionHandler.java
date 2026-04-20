package com.mipt.ailanakaramchakova.exception;

import com.mipt.ailanakaramchakova.dto.ProblemDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleTaskNotFound(TaskNotFoundException exception) {
        ProblemDetails details = new ProblemDetails(
          "https://example.com/errors/task-not-found",
          "Not Found",
          404,
          exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .contentType(MediaType.APPLICATION_PROBLEM_JSON)
          .body(details);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ProblemDetails> handleExternalApi(ExternalApiException exception) {
        ProblemDetails details = new ProblemDetails(
          "https://example.com/errors/external-failure",
          "External API Error",
          502,
          exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
          .contentType(MediaType.APPLICATION_PROBLEM_JSON)
          .body(details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGeneric(Exception exception) {
        ProblemDetails details = new ProblemDetails(
          "https://example.com/errors/internal",
          "Internal Server Error",
          500,
          "Unexpected error occurred"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .contentType(MediaType.APPLICATION_PROBLEM_JSON)
          .body(details);
    }
}
