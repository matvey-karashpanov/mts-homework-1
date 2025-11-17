package com.mipt.ailanakaramchakova.patterns;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CachingDecorator implements DataService {

  private final DataService wrapped;
  private final Map<String, String> cache = new HashMap<>();

  public CachingDecorator(DataService wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    if (cache.containsKey(key)) {
      return Optional.ofNullable(cache.get(key));
    }
    Optional<String> result = wrapped.findDataByKey(key);
    if (result.isPresent()) {
      cache.put(key, result.get());
    } else {
      cache.put(key, null);
    }
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    wrapped.saveData(key, data);
    cache.put(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    boolean result = wrapped.deleteData(key);
    cache.remove(key);
    return result;
  }
}
