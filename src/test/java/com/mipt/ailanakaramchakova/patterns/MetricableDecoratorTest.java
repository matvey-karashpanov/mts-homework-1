package com.mipt.ailanakaramchakova.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetricableDecoratorTest {

  private ByteArrayOutputStream metricOutput;
  private MetricableDecorator metricService;

  @BeforeEach
  void setUp() {
    metricOutput = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(metricOutput));

    DataService base = new SimpleDataService();
    metricService = new MetricableDecorator(base);
  }

  @Test
  void findDataByKey() {
    metricService.saveData("key", "value");
    metricService.findDataByKey("key");
    String output = metricOutput.toString();
    assertTrue(output.contains("The method was executed: PT"));
  }

  @Test
  void saveData() {
    metricService.saveData("key", "value");
    String output = metricOutput.toString();
    assertTrue(output.contains("The method was executed: PT"));
  }

  @Test
  void deleteData() {
    metricService.saveData("key", "value");
    metricService.deleteData("key");
    String output = metricOutput.toString();
    assertTrue(output.contains("The method was executed: PT"));
  }

  @Test
  void delegatesCorrectly() {
    metricService.saveData("key", "value");
    Optional<String> result = metricService.findDataByKey("key");
    assertTrue(result.isPresent());
    assertEquals("value", result.get());

    assertTrue(metricService.deleteData("key"));
    assertFalse(metricService.findDataByKey("key").isPresent());
  }
}
