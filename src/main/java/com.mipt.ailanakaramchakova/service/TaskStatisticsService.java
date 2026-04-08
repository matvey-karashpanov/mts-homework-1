package com.mipt.ailanakaramchakova.service;

import com.mipt.ailanakaramchakova.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Service to demonstrate @Qualifier usage.
 * Injects both primary and stub repositories.
 */
@Service
public class TaskStatisticsService {

  private static final Logger logger = LoggerFactory.getLogger(TaskStatisticsService.class);

  private final TaskRepository primaryRepository;
  private final TaskRepository stubRepository;

  public TaskStatisticsService(TaskRepository primaryRepository,
    @Qualifier("stubTaskRepository") TaskRepository stubRepository) {
    this.primaryRepository = primaryRepository;
    this.stubRepository = stubRepository;
  }

  public void compareRepositories() {
    logger.info("Primary count: {}", primaryRepository.findAll().size());
    logger.info("Stub count: {}", stubRepository.findAll().size());
  }
}
