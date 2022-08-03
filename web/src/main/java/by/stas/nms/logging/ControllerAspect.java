package by.stas.nms.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
    public void logAfterReturningValue(Object object) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        StringBuilder logString = new StringBuilder(request.getMethod()).append(" ")
                .append(request.getRequestURL());
        if (request.getQueryString() != null) {
            logString.append("?").append(request.getQueryString());
        }
        logString.append(" Response body: ").append(object);
        log.debug(logString.toString());
    }

    @AfterThrowing(value = "dataAccessMethods()", throwing = "e")
    public void logAfterThrowingException(Exception e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        StringBuilder logString = new StringBuilder(request.getMethod()).append(" ")
                .append(request.getRequestURL());
        if (request.getQueryString() != null) {
            logString.append("?").append(request.getQueryString());
        }
        logString.append(" Throws: ").append(e.getClass());
        log.error(logString.toString());
    }
}
