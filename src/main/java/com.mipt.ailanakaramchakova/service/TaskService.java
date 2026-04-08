package com.mipt.ailanakaramchakova.service;

import com.mipt.ailanakaramchakova.config.PrototypeScopedBean;
import com.mipt.ailanakaramchakova.model.Task;
import com.mipt.ailanakaramchakova.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for managing tasks.
 * Handles business logic and cache initialization.
 */
@Service
public class TaskService {

  private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

  private final TaskRepository repository;
  private final PrototypeScopedBean prototypeBean;
  private final Map<Long, Task> taskCache = new HashMap<>();

  @Value("${app.name}")
  private String appName;

  @Value("${app.version}")
  private String appVersion;

  public TaskService(TaskRepository repository, PrototypeScopedBean prototypeBean) {
    this.repository = repository;
    this.prototypeBean = prototypeBean;
  }

  @PostConstruct
  public void init() {
    Task task1 = new Task(1L, "Task 1", "Description 1", false);
    Task task2 = new Task(2L, "Task 2", "Description 2", true);

    taskCache.put(task1.getId(), task1);
    taskCache.put(task2.getId(), task2);

    logger.info("Cache initialized for {} v{}", appName, appVersion);
    logger.info("Prototype ID: {}", prototypeBean.getUuid());
  }

  @PreDestroy
  public void destroy() {
    logger.info("Destroying service. Cache size: {}", taskCache.size());
  }

  public List<Task> findAll() {
    return repository.findAll();
  }

  public Optional<Task> findById(Long id) {
    return repository.findById(id);
  }

  public Task save(Task task) {
    return repository.save(task);
  }

  public void deleteById(Long id) {
    repository.deleteById(id);
  }
}
