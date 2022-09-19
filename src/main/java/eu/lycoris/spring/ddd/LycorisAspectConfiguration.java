package eu.lycoris.spring.ddd;

import eu.lycoris.spring.ddd.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LycorisAspectConfiguration {

  @PersistenceContext private @NotNull EntityManager entityManager;

  private final @NotNull LycorisCommandService lycorisCommandService;

  public LycorisAspectConfiguration(@NotNull LycorisCommandService lycorisCommandService) {
    this.lycorisCommandService = lycorisCommandService;
  }

  @Nullable
  @Around("@annotation(LycorisFlushContext)")
  public Object flushContext(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    this.entityManager.flush();
    return result;
  }

  @Nullable
  @Around("@annotation(LycorisRetryCommand)")
  public Object saveCommandForRetry(ProceedingJoinPoint joinPoint) throws Throwable {
    if (joinPoint.getArgs().length != 1) {
      throw new IllegalArgumentException(
          "LycorisRetryCommand must be used on method with only one argument");
    }
    if (!(joinPoint.getArgs()[0] instanceof Command)) {
      throw new IllegalArgumentException(
          "LycorisRetryCommand must be used on method with one argument of type Command");
    }

    boolean isFromRetryService =
        Arrays.stream(Thread.currentThread().getStackTrace())
            .anyMatch(
                stackTrace ->
                    stackTrace
                        .getClassName()
                        .equals(LycorisCommandRetryService.class.getCanonicalName()));

    Command command = (Command) joinPoint.getArgs()[0];

    log.info("Executing command {}", command);

    try {
      Object result = joinPoint.proceed();

      log.info("Executed command {} returning {}", command, result);

      return result;
    } catch (Exception e) {
      if (!isFromRetryService) {
        this.lycorisCommandService.saveCommand(
            command,
            joinPoint.getSignature().getDeclaringType(),
            joinPoint.getSignature().getName());
      }
      throw e;
    }
  }
}
