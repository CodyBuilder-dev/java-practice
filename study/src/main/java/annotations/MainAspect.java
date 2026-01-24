package annotations;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class MainAspect {

  private static final Logger log =
      LoggerFactory.getLogger(Main.class);

  @Around("execution(@annotations.Main * *(..))")
  public Object doTrace(ProceedingJoinPoint joinPoint) throws Throwable {
    Object[] args = joinPoint.getArgs();

    log.info("############ {} ##############", joinPoint.getSignature());
    log.info("[trace] {} args={}", joinPoint.getSignature(), args);

    Object result = joinPoint.proceed();

    log.info("##########################");

    return result;
  }
}
