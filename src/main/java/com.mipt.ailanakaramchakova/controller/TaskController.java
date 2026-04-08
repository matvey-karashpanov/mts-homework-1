package com.mipt.ailanakaramchakova.controller;

import com.mipt.ailanakaramchakova.config.RequestScopedBean;
import com.mipt.ailanakaramchakova.dto.TaskCreateDto;
import com.mipt.ailanakaramchakova.dto.TaskResponseDto;
import com.mipt.ailanakaramchakova.dto.TaskUpdateDto;
import com.mipt.ailanakaramchakova.service.TaskService;
import com.mipt.ailanakaramchakova.validation.OnCreate;
import com.mipt.ailanakaramchakova.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Tag(name = "Task Management", description = "Operations for managing tasks")
public class TaskController {

  private final TaskService taskService;
  private final RequestScopedBean requestScopedBean;

  @Value("${app.api-version}")
  private String apiVersion;

  public TaskController(TaskService taskService, RequestScopedBean requestScopedBean) {
    this.taskService = taskService;
    this.requestScopedBean = requestScopedBean;
  }

  @GetMapping
  @Operation(summary = "Get all tasks", description = "Returns a list of all tasks")
  @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully")
  public ResponseEntity<List<TaskResponseDto>> getAll() {
    List<TaskResponseDto> tasks = taskService.findAll();
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Total-Count", String.valueOf(tasks.size()));
    headers.add("X-API-Version", apiVersion);
    return ResponseEntity.ok().headers(headers).body(tasks);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get task by ID", description = "Returns a single task by ID")
  @ApiResponse(responseCode = "200", description = "Task found")
  @ApiResponse(responseCode = "404", description = "Task not found")
  public ResponseEntity<TaskResponseDto> getById(@PathVariable Long id) {
    TaskResponseDto task = taskService.findById(id);
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-API-Version", apiVersion);
    return ResponseEntity.ok().headers(headers).body(task);
  }

  @PostMapping
  @Operation(summary = "Create new task", description = "Creates a new task")
  @ApiResponse(responseCode = "201", description = "Task created successfully")
  @ApiResponse(responseCode = "400", description = "Invalid input")
  public ResponseEntity<TaskResponseDto> create(
    @RequestBody @Validated(OnCreate.class) @Valid TaskCreateDto dto) {
    TaskResponseDto createdTask = taskService.create(dto);
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-API-Version", apiVersion);
    return ResponseEntity.ok().headers(headers).body(createdTask);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update task", description = "Updates an existing task")
  @ApiResponse(responseCode = "200", description = "Task updated successfully")
  @ApiResponse(responseCode = "404", description = "Task not found")
  public ResponseEntity<TaskResponseDto> update(
    @PathVariable Long id,
    @RequestBody @Validated(OnUpdate.class) @Valid TaskUpdateDto dto) {
    TaskResponseDto updatedTask = taskService.update(id, dto);
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-API-Version", apiVersion);
    return ResponseEntity.ok().headers(headers).body(updatedTask);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete task", description = "Deletes a task by ID")
  @ApiResponse(responseCode = "204", description = "Task deleted successfully")
  @ApiResponse(responseCode = "404", description = "Task not found")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    taskService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
