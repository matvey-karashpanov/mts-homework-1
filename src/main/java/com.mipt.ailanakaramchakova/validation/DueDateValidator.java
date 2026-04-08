package com.mipt.ailanakaramchakova.validation;

import com.mipt.ailanakaramchakova.dto.TaskUpdateDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * Validator for due date not before creation.
 */
public class DueDateValidator implements
  ConstraintValidator<DueDateNotBeforeCreation, TaskUpdateDto> {

  @Override
  public boolean isValid(TaskUpdateDto dto, ConstraintValidatorContext context) {
    if (dto == null) {
      return true;
    }

    LocalDate dueDate = dto.getDueDate();
    if (dueDate == null) {
      return true;
    }

    LocalDate today = LocalDate.now();
    if (dueDate.isBefore(today)) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("Due date cannot be in the past")
        .addPropertyNode("dueDate")
        .addConstraintViolation();
      return false;
    }

    return true;
  }
}
