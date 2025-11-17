package com.mipt.ailanakaramchakova.patterns;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class MetricableDecorator implements DataService {

  private final DataService wrapped;
  private final MetricService metricService = new MetricService();

  public MetricableDecorator(DataService wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    Instant start = Instant.now();
    Optional<String> result = wrapped.findDataByKey(key);
    Duration duration = Duration.between(start, Instant.now());
    metricService.sendMetric(duration);
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    Instant start = Instant.now();
    wrapped.saveData(key, data);
    Duration duration = Duration.between(start, Instant.now());
    metricService.sendMetric(duration);
  }

  @Override
  public boolean deleteData(String key) {
    Instant start = Instant.now();
    boolean result = wrapped.deleteData(key);
    Duration duration = Duration.between(start, Instant.now());
    metricService.sendMetric(duration);
    return result;
  }

  public static class MetricService {

    public void sendMetric(Duration duration) {
      System.out.println("The method was executed: " + duration.toString());
    }
  }
}
