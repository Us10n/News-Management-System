package by.stas.nms.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class ControllerAspect {

    @Pointcut("within(by.stas.nms.controller.*)")
    public void dataAccessMethods() {
    }

    @Before("dataAccessMethods())")
    public void logBeforeExecutionTime(JoinPoint jp) {
        log.debug("Executing method: " + jp.getSignature() +
                " with arguments " + List.of(jp.getArgs()));
    }

    @Around("dataAccessMethods()")
    public Object logExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        Object proceed = pjp.proceed();
        long end = System.nanoTime();
        log.debug("Execution of " + pjp.getSignature() + " took " +
                TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
        return proceed;
    }

    @AfterReturning(value = "dataAccessMethods()", returning = "object")
    public void logAfterReturningValue(JoinPoint jp, Object object) {
        log.debug("Method " + jp.getSignature() + " successfully executed and return value [" + object + "]");
    }

    @AfterThrowing(value = "dataAccessMethods()", throwing = "e")
    public void logAfterThrowingException(JoinPoint jp, Exception e) {
        log.error("Method " + jp.getSignature() + " throw " + e);
    }
}
