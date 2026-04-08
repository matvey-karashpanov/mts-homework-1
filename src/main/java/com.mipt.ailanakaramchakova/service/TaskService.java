package com.mipt.ailanakaramchakova.service;

import com.mipt.ailanakaramchakova.config.PrototypeScopedBean;
import com.mipt.ailanakaramchakova.dto.TaskCreateDto;
import com.mipt.ailanakaramchakova.dto.TaskResponseDto;
import com.mipt.ailanakaramchakova.dto.TaskUpdateDto;
import com.mipt.ailanakaramchakova.exception.TaskNotFoundException;
import com.mipt.ailanakaramchakova.mapper.TaskMapper;
import com.mipt.ailanakaramchakova.model.Task;
import com.mipt.ailanakaramchakova.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
  private final TaskMapper taskMapper;
  private final Map<Long, Task> taskCache = new HashMap<>();

  @Value("${app.name}")
  private String appName;

  @Value("${app.version}")
  private String appVersion;

  public TaskService(TaskRepository repository,
    PrototypeScopedBean prototypeBean,
    TaskMapper taskMapper) {
    this.repository = repository;
    this.prototypeBean = prototypeBean;
    this.taskMapper = taskMapper;
  }

  @PostConstruct
  public void init() {
    logger.info("Cache initialized for {} v{}", appName, appVersion);
    logger.info("Prototype ID: {}", prototypeBean.getUuid());
  }

  @PreDestroy
  public void destroy() {
    logger.info("Destroying service. Cache size: {}", taskCache.size());
  }

  public List<TaskResponseDto> findAll() {
    List<Task> tasks = repository.findAll();
    return tasks.stream()
      .map(taskMapper::toResponseDto)
      .collect(Collectors.toList());
  }

  public TaskResponseDto findById(Long id) {
    Task task = repository.findById(id)
      .orElseThrow(() -> new TaskNotFoundException(id));
    return taskMapper.toResponseDto(task);
  }

  public TaskResponseDto create(TaskCreateDto dto) {
    Task task = taskMapper.toEntity(dto);
    task.setCreatedAt(LocalDateTime.now());
    Task savedTask = repository.save(task);
    return taskMapper.toResponseDto(savedTask);
  }

  public TaskResponseDto update(Long id, TaskUpdateDto dto) {
    Task task = repository.findById(id)
      .orElseThrow(() -> new TaskNotFoundException(id));
    taskMapper.updateEntityFromDto(dto, task);
    Task updatedTask = repository.save(task);
    return taskMapper.toResponseDto(updatedTask);
  }

  public void deleteById(Long id) {
    if (!repository.deleteById(id)) {
      throw new TaskNotFoundException(id);
    }
  }
}
