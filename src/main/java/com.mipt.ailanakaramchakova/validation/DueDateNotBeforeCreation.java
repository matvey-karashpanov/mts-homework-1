package com.mipt.ailanakaramchakova.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom validator annotation for due date validation.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DueDateValidator.class)
@Documented
public @interface DueDateNotBeforeCreation {

  String message() default "Due date cannot be before creation date";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
