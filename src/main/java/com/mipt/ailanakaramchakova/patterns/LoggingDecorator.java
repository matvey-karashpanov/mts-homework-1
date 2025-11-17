package com.mipt.ailanakaramchakova.patterns;

import java.util.Optional;

public class LoggingDecorator implements DataService {

  private final DataService wrapped;

  public LoggingDecorator(DataService wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    System.out.println("Logging: findDataByKey(" + key + ")");
    return wrapped.findDataByKey(key);
  }

  @Override
  public void saveData(String key, String data) {
    System.out.println("Logging: saveData(" + key + ", " + data + ")");
    wrapped.saveData(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    System.out.println("Logging: deleteData(" + key + ")");
    return wrapped.deleteData(key);
  }
}
