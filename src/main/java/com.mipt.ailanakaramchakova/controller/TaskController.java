package com.mipt.ailanakaramchakova.controller;

import com.mipt.ailanakaramchakova.config.RequestScopedBean;
import com.mipt.ailanakaramchakova.dto.TaskDto;
import com.mipt.ailanakaramchakova.model.Task;
import com.mipt.ailanakaramchakova.service.TaskService;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Task management.
 * Provides CRUD endpoints for tasks.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskService taskService;
  private final RequestScopedBean requestScopedBean;

  @Autowired
  public TaskController(TaskService taskService, RequestScopedBean requestScopedBean) {
    this.taskService = taskService;
    this.requestScopedBean = requestScopedBean;
  }

  @GetMapping
  public ResponseEntity<List<TaskDto>> getAll() {
    List<Task> tasks = taskService.findAll();
    List<TaskDto> taskDtos = tasks.stream()
      .map(this::convertToDto)
      .collect(Collectors.toList());
    return ResponseEntity.ok(taskDtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskDto> getById(@PathVariable Long id) {
    return taskService.findById(id)
      .map(this::convertToDto)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<TaskDto> create(@RequestBody TaskDto taskDto) {
    Task task = convertToEntity(taskDto);
    Task savedTask = taskService.save(task);
    return ResponseEntity.ok(convertToDto(savedTask));
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskDto> update(@PathVariable Long id, @RequestBody TaskDto taskDto) {
    taskDto.setId(id);
    Task task = convertToEntity(taskDto);
    Task updatedTask = taskService.save(task);
    return ResponseEntity.ok(convertToDto(updatedTask));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    taskService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  private TaskDto convertToDto(Task task) {
    return new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted());
  }

  private Task convertToEntity(TaskDto dto) {
    return new Task(dto.getId(), dto.getTitle(), dto.getDescription(), dto.isCompleted());
  }
}
