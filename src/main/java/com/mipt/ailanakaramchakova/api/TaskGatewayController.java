package com.mipt.ailanakaramchakova.api;

import com.mipt.ailanakaramchakova.dto.TaskCreateRequest;
import com.mipt.ailanakaramchakova.dto.TaskListDto;
import com.mipt.ailanakaramchakova.dto.TaskResponseDto;
import com.mipt.ailanakaramchakova.service.TasksGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskGatewayController {
    private final TasksGatewayService tasksGatewayService;

    public TaskGatewayController(TasksGatewayService tasksGatewayService) {
        this.tasksGatewayService = tasksGatewayService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskCreateRequest request) {
        TaskResponseDto created = tasksGatewayService.createTask(request);
        URI location = URI.create("/api/v1/tasks/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable Long id) {
        TaskResponseDto task = tasksGatewayService.getTask(id);
        if (task.id() == null) {
            return ResponseEntity.ok(task);
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public List<TaskListDto> listTasks(
      @RequestParam(required = false) Boolean completed,
      @RequestParam(defaultValue = "100") Integer limit) {
        return tasksGatewayService.listTasks(completed, limit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        tasksGatewayService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
