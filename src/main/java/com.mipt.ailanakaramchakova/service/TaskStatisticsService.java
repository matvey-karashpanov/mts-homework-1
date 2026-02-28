package com.mipt.ailanakaramchakova.service;

import com.mipt.ailanakaramchakova.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Service to demonstrate @Qualifier usage.
 * Injects both primary and stub repositories.
 */
@Service
public class TaskStatisticsService {
    private final TaskRepository primaryRepository;
    private final TaskRepository stubRepository;

    public TaskStatisticsService(TaskRepository primaryRepository,
        @Qualifier("stubTaskRepository") TaskRepository stubRepository) {
        this.primaryRepository = primaryRepository;
        this.stubRepository = stubRepository;
    }

    public void compareRepositories() {
        System.out.println("Primary count: " + primaryRepository.findAll().size());
        System.out.println("Stub count: " + stubRepository.findAll().size());
    }
}
