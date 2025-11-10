package com.mipt.ailanakaramchakova.validationclass;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mipt.ailanakaramchakova.validationclass.result.ValidationResult;
import org.junit.jupiter.api.Test;

public class ValidatorTest {

  @Test
  public void testValidUser() {
    TestUser user = new TestUser("S.coups", "scoups@example.com", 30, "s.coups08081995");

    ValidationResult result = Validator.validate(user);
    assertTrue(result.isValid());
    assertTrue(result.getErrors().isEmpty());
  }

  @Test
  public void testNameIsNull() {
    TestUser user = new TestUser(null, "scoups@example.com", 30, "s.coups08081995");

    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("The name cannot be null"));
  }

  @Test
  public void testNameLong() {
    TestUser user = new TestUser("S.coups".repeat(10), "scoups@example.com", 30, "s.coups08081995");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("The name must be between 2 and 50 characters long"));
  }

  @Test
  public void testEmailIsNull() {
    TestUser user = new TestUser("S.coups", null, 30, "s.coups08081995");

    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Email cannot be null"));
  }

  @Test
  public void testInvalidEmail() {
    TestUser user = new TestUser("S.coups", "scoups", 30, "s.coups08081995");

    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid(), "Email is invalid");
    assertTrue(result.getErrors().contains("Invalid email format"));
  }

  @Test
  public void testAgeOutOfRange() {
    TestUser user = new TestUser("S.coups", "scoups@example.com", 333, "s.coups08081995");

    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("The age should be between 0 and 150"));
  }

  @Test
  public void testPasswordShort() {
    TestUser user = new TestUser("S.coups", "scoups@example.com", 30, "08");

    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertTrue(
        result.getErrors().contains("The password must be between 6 and 20 characters long"));
  }

  @Test
  public void testMultipleErrors() {
    TestUser user = new TestUser(null, "scoups", 333, "08");

    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("The name cannot be null"));
    assertTrue(result.getErrors().contains("Invalid email format"));
    assertTrue(result.getErrors().contains("The age should be between 0 and 150"));
    assertTrue(
        result.getErrors().contains("The password must be between 6 and 20 characters long"));
  }
}
