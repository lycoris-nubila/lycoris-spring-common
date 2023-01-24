package eu.lycoris.spring.common;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LycorisPersistenceAspect {

  @PersistenceContext private @NotNull EntityManager entityManager;

  @Nullable @Around("@annotation(eu.lycoris.spring.common.LycorisFlushContext)")
  public Object flushContext(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    this.entityManager.flush();
    return result;
  }
}
