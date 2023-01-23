package eu.lycoris.spring.cqrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.lycoris.spring.cqrs.model.Command;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Slf4j
@Aspect
@Component
@ConditionalOnBean({Job.class, JobRepository.class})
public class LycorisCommandAspect {

  private final @NotNull Job commandJob;

  private final @NotNull JobRepository jobRepository;

  private final @NotNull ObjectMapper objectMapper;

  public LycorisCommandAspect(
      @Qualifier("commandJob") @NotNull Job commandJob,
      @NotNull JobRepository jobRepository,
      @NotNull ObjectMapper objectMapper) {
    this.jobRepository = jobRepository;
    this.commandJob = commandJob;
    this.objectMapper = objectMapper;
  }

  @Nullable
  @Around("@annotation(eu.lycoris.spring.cqrs.LycorisRetryCommand)")
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
      if (command.getId() == null) {
        throw t;
      }

      JobParameters parameters =
          new JobParametersBuilder()
              .addString("command", this.objectMapper.writeValueAsString(command))
              .addString("id", command.getId().toString())
              .toJobParameters();

      if (this.jobRepository.isJobInstanceExists(this.commandJob.getName(), parameters)) {
        throw t;
      }

      this.jobRepository.createJobExecution(this.commandJob.getName(), parameters);

      throw t;
    }
  }
}
