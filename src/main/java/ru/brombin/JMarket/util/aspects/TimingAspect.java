package ru.brombin.JMarket.util.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
public class TimingAspect {
    @Around("@annotation(ru.brombin.JMarket.util.annotation.Timed)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        long elapsedTime = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();
        } finally {
            elapsedTime = System.currentTimeMillis() - start;
            log.info("Method {} executed in {} ms", joinPoint.getSignature(), elapsedTime);
        }
        return result;
    }
}
