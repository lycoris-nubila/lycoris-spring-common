package eu.lycoris.spring.ddd;

import eu.lycoris.spring.ddd.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Aspect
@Component
public class LycorisAspectConfiguration {

  @PersistenceContext private EntityManager entityManager;

  @Autowired private LycorisCommandService lycorisCommandService;

  @Around("@annotation(LycorisFlushContext)")
  public Object flushContext(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    this.entityManager.flush();
    return result;
  }

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

    Command command = (Command) joinPoint.getArgs()[0];

    log.debug("Executing command {}", command);

    try {
      Object result = joinPoint.proceed();

      if (command.getFuture() != null) {
        command.getFuture().complete(false);
      }

      log.debug("Executed command {} returning {}", command, result);

      return result;
    } catch (Exception e) {
      if (command.getFuture() != null) {
        command.getFuture().complete(true);
      } else {
        this.lycorisCommandService.saveCommand(
            command,
            joinPoint.getSignature().getDeclaringType(),
            joinPoint.getSignature().getName());
      }
      throw e;
    }
  }
}
