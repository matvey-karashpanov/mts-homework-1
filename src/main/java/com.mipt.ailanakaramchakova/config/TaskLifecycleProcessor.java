package com.mipt.ailanakaramchakova.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * BeanPostProcessor to log bean creation and initialization.
 * Tracks TaskService and TaskRepository beans.
 */
@Component
public class TaskLifecycleProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
    throws BeansException {
    if (bean.getClass().getName().contains("TaskService") ||
      bean.getClass().getName().contains("TaskRepository")) {
      System.out.println("[LIFECYCLE] Creating bean: " + beanName);
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean.getClass().getName().contains("TaskService") ||
      bean.getClass().getName().contains("TaskRepository")) {
      System.out.println("[LIFECYCLE] Initialized bean: " + beanName);
    }
    return bean;
  }
}
