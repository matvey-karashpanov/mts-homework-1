package com.mipt.ailanakaramchakova.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoggingDecoratorTest {

  private ByteArrayOutputStream logContent;
  private LoggingDecorator loggingService;

  @BeforeEach
  void setUp() {
    logContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(logContent));

    DataService base = new SimpleDataService();
    loggingService = new LoggingDecorator(base);
  }

  @Test
  void findDataByKey() {
    loggingService.saveData("key", "value");
    loggingService.findDataByKey("key");
    String output = logContent.toString();
    assertTrue(output.contains("Logging: findDataByKey(key)"));
  }

  @Test
  void saveData() {
    loggingService.saveData("key", "value");
    String output = logContent.toString();
    assertTrue(output.contains("Logging: saveData(key, value)"));

  }

  @Test
  void deleteData() {
    loggingService.saveData("key", "value");
    loggingService.deleteData("key");
    String output = logContent.toString();
    assertTrue(output.contains("Logging: deleteData(key)"));
  }

  @Test
  void delegatesCorrectly() {
    loggingService.saveData("key", "value");
    Optional<String> result = loggingService.findDataByKey("key");
    assertTrue(result.isPresent());
    assertEquals("value", result.get());

    assertTrue(loggingService.deleteData("key"));
    assertFalse(loggingService.findDataByKey("key").isPresent());
  }
}
