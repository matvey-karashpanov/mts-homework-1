package com.mipt.ailanakaramchakova.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging service method execution.
 * Uses @Around advice to log start, end and duration.
 */
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.mipt.ailanakaramchakova.service.TaskService.*(..))")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("[LOG] Start: " + joinPoint.getSignature().getName());
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            System.out.println("[LOG] End: " + joinPoint.getSignature().getName() +
                ", Result: " + result);
            return result;
        } catch (Throwable e) {
            System.out.println("[LOG] Error: " + e.getMessage());
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - start;
            System.out.println("[LOG] Duration: " + duration + "ms");
        }
    }
}
