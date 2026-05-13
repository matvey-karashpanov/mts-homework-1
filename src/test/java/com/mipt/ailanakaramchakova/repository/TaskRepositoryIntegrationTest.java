package com.mipt.ailanakaramchakova.repository;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mipt.ailanakaramchakova.model.Priority;
import com.mipt.ailanakaramchakova.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

/**
 * Integration tests for TaskRepository with PostgreSQL in Docker (Testcontainers). Verifies JPA
 * mappings and custom @Query methods using a real database instead of H2.
 */
@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
public class TaskRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
      .withDatabaseName("testdb")
      .withUsername("test")
      .withPassword("test");

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.flyway.enabled", () -> false);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testFindTasksDueInNextSevenDays_WithTestcontainers() {
        Task task1 = new Task(null, "Task 1", "Description 1", false);
        task1.setPriority(Priority.MEDIUM);
        task1.setDueDate(LocalDate.now().plusDays(3));

        Task task2 = new Task(null, "Task 2", "Description 2", false);
        task2.setPriority(Priority.HIGH);
        task2.setDueDate(LocalDate.now().plusDays(30));

        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> dueSoon = taskRepository.findTasksDueInNextSevenDays(
          LocalDate.now(),
          LocalDate.now().plusDays(7)
        );

        assertEquals(1, dueSoon.size());
        assertEquals("Task 1", dueSoon.get(0).getTitle());
        assertEquals(Priority.MEDIUM, dueSoon.get(0).getPriority());
    }

    @Test
    public void testSaveAndFind_WithTestcontainers() {
        Task task = new Task(null, "Test Task", "Test Description", false);
        task.setPriority(Priority.HIGH);
        task.setDueDate(LocalDate.now().plusDays(7));

        Task saved = taskRepository.save(task);
        Task found = taskRepository.findById(saved.getId()).orElseThrow();

        assertNotNull(saved.getId());
        assertEquals("Test Task", found.getTitle());
        assertEquals(Priority.HIGH, found.getPriority());
    }

    @Test
    public void testFindByPriority_WithTestcontainers() {
        Task task1 = new Task(null, "High Priority Task", "Desc", false);
        task1.setPriority(Priority.HIGH);

        Task task2 = new Task(null, "Low Priority Task", "Desc", false);
        task2.setPriority(Priority.LOW);

        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> highPriorityTasks = taskRepository.findByPriority(Priority.HIGH);

        assertEquals(1, highPriorityTasks.size());
        assertEquals("High Priority Task", highPriorityTasks.get(0).getTitle());
    }
}
