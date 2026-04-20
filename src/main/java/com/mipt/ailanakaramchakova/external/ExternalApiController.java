package com.mipt.ailanakaramchakova.external;

import com.mipt.ailanakaramchakova.dto.ProblemDetails;
import com.mipt.ailanakaramchakova.dto.TaskCreateRequest;
import com.mipt.ailanakaramchakova.dto.TaskListDto;
import com.mipt.ailanakaramchakova.dto.TaskResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/external/v1")
public class ExternalApiController {

    private final Map<Long, TaskResponseDto> taskStore = new ConcurrentHashMap<>();
    private final AtomicLong sequenceGenerator = new AtomicLong(100);

    public ExternalApiController() {
        taskStore.put(1L, new TaskResponseDto(1L, "Read book", "Spring Security", false));
        taskStore.put(2L, new TaskResponseDto(2L, "Buy milk", "2 liters", true));
    }

    @PostMapping(value = "/tasks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskCreateRequest request) {
        long newId = sequenceGenerator.incrementAndGet();
        TaskResponseDto created = new TaskResponseDto(newId, request.title(), request.description(),
          false);
        taskStore.put(newId, created);
        URI location = URI.create("/external/v1/tasks/" + newId);
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id) {
        TaskResponseDto task = taskStore.get(id);
        if (task == null) {
            ProblemDetails details = new ProblemDetails(
              "https://example.com/errors/not-found",
              "Not Found",
              404,
              "Task with id " + id + " not found"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .contentType(MediaType.APPLICATION_PROBLEM_JSON)
              .body(details);
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping("/tasks")
    public List<TaskListDto> listTasks(
      @RequestParam(required = false) Boolean completed,
      @RequestParam(defaultValue = "100") Integer limit) {
        return taskStore.values().stream()
          .filter(task -> completed == null || task.completed() == completed)
          .limit(limit)
          .map(task -> new TaskListDto(task.id(), task.title(), task.completed()))
          .toList();
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskStore.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unstable")
    public ResponseEntity<?> unstableMode(@RequestParam String mode) throws InterruptedException {
        if (mode.equals("timeout")) {
            Thread.sleep(5000);
            return ResponseEntity.ok("slow");
        }
        if (mode.equals("500")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error");
        }
        if (mode.equals("429")) {
            return ResponseEntity.status(429).header("Retry-After", "3")
              .body("Rate limit exceeded");
        }
        if (mode.equals("html")) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
              .contentType(MediaType.TEXT_HTML)
              .body("<html><body><h1>Bad Gateway</h1></body></html>");
        }
        return ResponseEntity.ok("stable");
    }
}
