package com.mipt.ailanakaramchakova.service;

import com.mipt.ailanakaramchakova.dto.TaskResponseDto;
import com.mipt.ailanakaramchakova.dto.TaskUpdateDto;
import com.mipt.ailanakaramchakova.exception.TaskNotFoundException;
import com.mipt.ailanakaramchakova.mapper.TaskMapper;
import com.mipt.ailanakaramchakova.model.Priority;
import com.mipt.ailanakaramchakova.model.Task;
import com.mipt.ailanakaramchakova.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for TaskService using Mockito (London School approach). Tests business logic in
 * isolation by mocking dependencies and verifying interactions.
 */
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Captor
    private ArgumentCaptor<Task> taskCaptor;

    @Test
    public void givenExistingTask_whenUpdateStatus_thenTaskCompletedAndRepositoryCalled() {
        Long taskId = 1L;
        Task existingTask = new Task(taskId, "Test Task", "Description", false);
        existingTask.setPriority(Priority.MEDIUM);
        existingTask.setCreatedAt(LocalDateTime.now());

        TaskUpdateDto updateDto = new TaskUpdateDto();
        updateDto.setCompleted(true);

        TaskResponseDto responseDto = new TaskResponseDto(
          taskId, "Test Task", "Description", true,
          LocalDateTime.now(), null, Priority.MEDIUM, new HashSet<>()
        );

        when(repository.findById(taskId)).thenReturn(Optional.of(existingTask));

        when(repository.save(any(Task.class))).thenAnswer(invocation -> {
            Task taskToSave = invocation.getArgument(0);
            taskToSave.setCompleted(true);
            return taskToSave;
        });

        doAnswer(invocation -> {
            TaskUpdateDto dto = invocation.getArgument(0);
            Task task = invocation.getArgument(1);
            if (dto.getCompleted() != null) {
                task.setCompleted(dto.getCompleted());
            }
            if (dto.getTitle() != null) {
                task.setTitle(dto.getTitle());
            }
            return null;
        }).when(taskMapper).updateEntityFromDto(any(TaskUpdateDto.class), any(Task.class));

        when(taskMapper.toResponseDto(any(Task.class))).thenReturn(responseDto);

        TaskResponseDto result = taskService.update(taskId, updateDto);

        assertNotNull(result);
        assertTrue(result.isCompleted());

        verify(repository, times(1)).findById(taskId);
        verify(repository, times(1)).save(taskCaptor.capture());

        Task savedTask = taskCaptor.getValue();
        assertTrue(savedTask.isCompleted(), "Task should be marked as completed");
        assertEquals(taskId, savedTask.getId());
    }

    @Test
    public void givenNonExistingTask_whenUpdate_thenTaskNotFoundExceptionThrown() {
        Long taskId = 999L;
        TaskUpdateDto updateDto = new TaskUpdateDto();
        updateDto.setTitle("Updated Title");

        when(repository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.update(taskId, updateDto);
        });

        verify(repository, times(1)).findById(taskId);
        verify(repository, never()).save(any(Task.class));
    }
}
