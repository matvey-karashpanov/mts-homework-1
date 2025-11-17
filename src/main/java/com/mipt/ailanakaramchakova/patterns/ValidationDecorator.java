package com.mipt.ailanakaramchakova.patterns;

import java.util.Optional;

public class ValidationDecorator implements DataService {

  private final DataService wrapped;

  public ValidationDecorator(DataService wrapped) {
    this.wrapped = wrapped;
  }

  private void validateKey(String key) {
    if (key == null || key.isBlank()) {
      throw new IllegalArgumentException("Key must not be null or blank");
    }
  }

  private void validateData(String data) {
    if (data == null) {
      throw new IllegalArgumentException("Data must not be null");
    }
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    validateKey(key);
    return wrapped.findDataByKey(key);
  }

  @Override
  public void saveData(String key, String data) {
    validateKey(key);
    validateData(data);
    wrapped.saveData(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    validateKey(key);
    return wrapped.deleteData(key);
  }
}
