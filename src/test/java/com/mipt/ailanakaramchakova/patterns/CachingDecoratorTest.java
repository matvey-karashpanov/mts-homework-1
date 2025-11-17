package com.mipt.ailanakaramchakova.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CachingDecoratorTest {

  private SimpleDataService baseService;
  private CachingDecorator cachingService;

  @BeforeEach
  void setUp() {
    baseService = new SimpleDataService();
    cachingService = new CachingDecorator(baseService);
  }

  @Test
  void findDataByKey() {
    baseService.saveData("key", "value");

    Optional<String> first = cachingService.findDataByKey("key");
    assertTrue(first.isPresent());
    assertEquals("value", first.get());

    baseService.deleteData("key");
    Optional<String> second = cachingService.findDataByKey("key");
    assertTrue(second.isPresent());
    assertEquals("value", second.get());
  }

  @Test
  void saveData() {
    cachingService.saveData("key", "value");
    Optional<String> result = cachingService.findDataByKey("key");
    assertTrue(result.isPresent());
    assertEquals("value", result.get());
  }

  @Test
  void deleteData() {
    cachingService.saveData("key", "value");
    cachingService.findDataByKey("key");

    assertTrue(cachingService.deleteData("key"));
    Optional<String> result = cachingService.findDataByKey("key");
    assertFalse(result.isPresent());
  }
}
