package com.mipt.ailanakaramchakova.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class ValidationDecoratorTest {

  private final DataService service = new ValidationDecorator(new SimpleDataService());

  @Test
  void findDataByKey_throwsOnNullKey() {
    assertThrows(IllegalArgumentException.class, () -> service.findDataByKey(null));
  }

  @Test
  void findDataByKey_throwsOnBlankKey() {
    assertThrows(IllegalArgumentException.class, () -> service.findDataByKey("   "));
  }

  @Test
  void saveData_throwsOnNullKey() {
    assertThrows(IllegalArgumentException.class, () -> service.saveData(null, "data"));
  }

  @Test
  void saveData_throwsOnBlankKey() {
    assertThrows(IllegalArgumentException.class, () -> service.saveData("   ", "data"));
  }

  @Test
  void saveData_throwsOnNullData() {
    assertThrows(IllegalArgumentException.class, () -> service.saveData("key", null));
  }

  @Test
  void deleteData_throwsOnNullKey() {
    assertThrows(IllegalArgumentException.class, () -> service.deleteData(null));
  }

  @Test
  void deleteData_throwsOnBlankKey() {
    assertThrows(IllegalArgumentException.class, () -> service.deleteData("   "));
  }

  @Test
  void validOperationsWork() {
    service.saveData("key", "value");
    Optional<String> result = service.findDataByKey("key");
    assertTrue(result.isPresent());
    assertEquals("value", result.get());

    assertTrue(service.deleteData("key"));
    assertFalse(service.findDataByKey("key").isPresent());
  }
}
