package com.eazybytes.eazyschool.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @Around("execution(* com.eazybytes.eazyschool..*.*(..))")
    public Object log(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{

        log.info(proceedingJoinPoint.getSignature().toString()+"Method Execution Starts");
        Instant start=Instant.now();
        Object returnObj=proceedingJoinPoint.proceed();
        Instant finish=Instant.now();
        long timeElapsed= Duration.between(start,finish).toMillis();
        log.info("Time Took to execute "+proceedingJoinPoint.getSignature().toString()+"method is: "+timeElapsed);
        log.info(proceedingJoinPoint.getSignature().toString()+"Method execution is end");
        return returnObj;
    }

    @AfterThrowing(value = "execution(* com.eazybytes.eazyschool..*.*(..))", throwing="ex")
    public void logException(JoinPoint joinPoint,Exception ex){

        log.error(joinPoint.getSignature().toString()+"An Exception Occured due to: "+ex.getMessage());

    }
}
