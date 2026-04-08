package com.mipt.ailanakaramchakova.mapper;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mipt.ailanakaramchakova.dto.TaskCreateDto;
import com.mipt.ailanakaramchakova.dto.TaskResponseDto;
import com.mipt.ailanakaramchakova.dto.TaskUpdateDto;
import com.mipt.ailanakaramchakova.model.Priority;
import com.mipt.ailanakaramchakova.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests for TaskMapper MapStruct implementation.
 * Covers entity-DTO conversions.
 */
@SpringBootTest
public class TaskMapperTest {

  @Autowired
  private TaskMapper taskMapper;

  @Test
  public void testToEntity_FromCreateDto() {
    TaskCreateDto dto = new TaskCreateDto();
    dto.setTitle("Test Task");
    dto.setDescription("Test Description");
    dto.setDueDate(LocalDate.now().plusDays(7));
    dto.setPriority(Priority.HIGH);
    Set<String> tags = new HashSet<>();
    tags.add("important");
    tags.add("urgent");
    dto.setTags(tags);

    Task task = taskMapper.toEntity(dto);

    assertNotNull(task);
    assertEquals("Test Task", task.getTitle());
    assertEquals("Test Description", task.getDescription());
    assertEquals(Priority.HIGH, task.getPriority());
    assertEquals(2, task.getTags().size());
    assertNull(task.getId());
  }

  @Test
  public void testToResponseDto_FromTask() {
    Set<String> tags = new HashSet<>();
    tags.add("test");
    Task task = new Task(
      1L, "Test Task", "Test Description", false,
      LocalDateTime.now(), LocalDate.now().plusDays(7),
      Priority.MEDIUM, tags
    );

    TaskResponseDto dto = taskMapper.toResponseDto(task);

    assertNotNull(dto);
    assertEquals(1L, dto.getId());
    assertEquals("Test Task", dto.getTitle());
    assertEquals("Test Description", dto.getDescription());
    assertEquals(Priority.MEDIUM, dto.getPriority());
    assertEquals(1, dto.getTags().size());
    assertNotNull(dto.getCreatedAt());
  }

  @Test
  public void testUpdateEntityFromDto() {
    Set<String> originalTags = new HashSet<>();
    originalTags.add("original");
    Task task = new Task(
      1L, "Original Title", "Original Description", false,
      LocalDateTime.now(), LocalDate.now(),
      Priority.LOW, originalTags
    );

    TaskUpdateDto dto = new TaskUpdateDto();
    dto.setTitle("Updated Title");
    dto.setCompleted(true);
    dto.setPriority(Priority.HIGH);
    Set<String> newTags = new HashSet<>();
    newTags.add("updated");
    dto.setTags(newTags);

    taskMapper.updateEntityFromDto(dto, task);

    assertEquals("Updated Title", task.getTitle());
    assertTrue(task.isCompleted());
    assertEquals(Priority.HIGH, task.getPriority());
    assertEquals("updated", task.getTags().iterator().next());
    assertNotNull(task.getCreatedAt());
  }

  @Test
  public void testUpdateEntityFromDto_PartialUpdate() {
    Task task = new Task(
      1L, "Original Title", "Original Description", false,
      LocalDateTime.now(), LocalDate.now(),
      Priority.LOW, new HashSet<>()
    );

    TaskUpdateDto dto = new TaskUpdateDto();
    dto.setCompleted(true);

    taskMapper.updateEntityFromDto(dto, task);

    assertEquals("Original Title", task.getTitle());
    assertEquals("Original Description", task.getDescription());
    assertTrue(task.isCompleted());
    assertEquals(Priority.LOW, task.getPriority());
  }
}
