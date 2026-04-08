package com.mipt.ailanakaramchakova.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging service method execution.
 * Uses @Around advice to log start, end and duration.
 */
@Aspect
@Component
public class LoggingAspect {

  private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

  @Around("execution(* com.mipt.ailanakaramchakova.service.TaskService.*(..))")
  public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
    logger.info("Start: {}", joinPoint.getSignature().getName());
    long start = System.currentTimeMillis();
    try {
      Object result = joinPoint.proceed();
      logger.info("End: {}, Result: {}", joinPoint.getSignature().getName(), result);
      return result;
    } catch (Throwable e) {
      logger.error("Error: {}", e.getMessage());
      throw e;
    } finally {
      long duration = System.currentTimeMillis() - start;
      logger.info("Duration: {}ms", duration);
    }
  }
}
