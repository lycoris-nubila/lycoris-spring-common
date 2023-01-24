package eu.lycoris.spring.cqrs;

import eu.lycoris.spring.cqrs.model.Command;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LycorisCommandAspect {

  private final @NotNull LycorisCommandService lycorisCommandService;

  public LycorisCommandAspect(@NotNull LycorisCommandService lycorisCommandService) {
    this.lycorisCommandService = lycorisCommandService;
  }

  @Nullable @Around("@annotation(eu.lycoris.spring.cqrs.LycorisRetryCommand)")
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
    command.setServiceClass(joinPoint.getSignature().getDeclaringType());
    command.setMethodName(joinPoint.getSignature().getName());
    if (command.getId() == null) {
      command.setId(UUID.randomUUID());
    }

    log.debug("Executing command {}", command);

    try {
      return joinPoint.proceed();
    } catch (Throwable t) {
      this.lycorisCommandService.saveCommand(command);
      throw t;
    }
  }
}
