package com.mipt.ailanakaramchakova.validationclass.result;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

  private boolean isValid = true;
  private final List<String> errors = new ArrayList<>();

  public void addError(String error) {
    errors.add(error);
    isValid = false;
  }

  public boolean isValid() {
    return isValid;
  }

  public List<String> getErrors() {
    return new ArrayList<>(errors);
  }
}
