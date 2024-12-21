package mockcote.statservice.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* mockcote.statservice.controller..*(..)) || execution(* mockcote.statservice.service..*(..))")
    public void controllerAndServiceMethods() {}

    @Before("controllerAndServiceMethods()")
    public void logMethodInputs(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("메서드 호출: {} - 입력 파라미터: {}", methodName, args);
    }

    @AfterReturning(value = "controllerAndServiceMethods()", returning = "result")
    public void logMethodOutputs(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();

        log.info("메서드 반환: {} - 반환값: {}", methodName, result);
    }

}
