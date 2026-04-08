package com.mipt.ailanakaramchakova.exception;

/**
 * Exception thrown when a task is not found.
 */
public class TaskNotFoundException extends BusinessException {

  public TaskNotFoundException(Long id) {
    super("Task not found with id: " + id);
  }
}
