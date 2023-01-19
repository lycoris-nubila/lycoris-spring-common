package eu.lycoris.spring.cqrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.lycoris.spring.cqrs.model.Command;
import eu.lycoris.spring.cqrs.model.FailedCommand;
import eu.lycoris.spring.property.LycorisProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class LycorisCommandRetryService {

  private final @NotNull ObjectMapper objectMapper;

  private final @NotNull LycorisProperties lycorisProperties;

  private final @NotNull ListableBeanFactory listableBeanFactory;

  private final @NotNull FailedCommandRepository failedCommandRepository;

  public LycorisCommandRetryService(
      @NotNull ObjectMapper objectMapper,
      @NotNull LycorisProperties lycorisProperties,
      @NotNull ListableBeanFactory listableBeanFactory,
      @NotNull FailedCommandRepository failedCommandRepository) {
    this.objectMapper = objectMapper;
    this.lycorisProperties = lycorisProperties;
    this.listableBeanFactory = listableBeanFactory;
    this.failedCommandRepository = failedCommandRepository;
  }

  @SuppressWarnings("unchecked")
  @Transactional(propagation = Propagation.REQUIRED)
  @Scheduled(
      fixedDelayString = "${lycoris.command-retry.delay-millisec:10000}",
      initialDelayString = "${lycoris.command-retry.delay-millisec:10000}")
  public void retryFailedCommands() {
    List<FailedCommand> failedCommands = this.failedCommandRepository.findAll();
    for (FailedCommand failedCommand : failedCommands) {
      if (failedCommand.getNextRetryTime() == null) {
        log.info(
            "Command {} as reached maximum attempt ({})",
            failedCommand,
            this.lycorisProperties.getCommandRetry().getMaxAttempts());
        continue;
      } else if (failedCommand.getNextRetryTime().isAfter(Instant.now())) {
        log.info(
            "Command {} will be retried at ({})", failedCommand, failedCommand.getNextRetryTime());
        continue;
      }

      try {
        Class<? extends Command> commandClass =
            (Class<? extends Command>) Class.forName(failedCommand.getCommmandClass());
        Command command = this.objectMapper.readValue(failedCommand.getCommand(), commandClass);
        command.setFuture(new CompletableFuture<>());

        Class<?> serviceClass = Class.forName(failedCommand.getServiceClass());

        Method serviceMethod =
            serviceClass.getMethod(failedCommand.getServiceMethodName(), commandClass);

        serviceMethod.invoke(this.listableBeanFactory.getBean(serviceClass), command);

        if (command.getFuture() != null && BooleanUtils.isTrue(command.getFuture().get())) {
          failedCommand.scheduleNextRetry(
              this.lycorisProperties.getCommandRetry().getMaxAttempts(),
              this.lycorisProperties.getCommandRetry().getBackoffMillisec());
          this.failedCommandRepository.save(failedCommand);
        } else {
          this.failedCommandRepository.delete(failedCommand);
        }
      } catch (Exception e) {
        failedCommand.scheduleNextRetry(
            this.lycorisProperties.getCommandRetry().getMaxAttempts(),
            this.lycorisProperties.getCommandRetry().getBackoffMillisec());
        this.failedCommandRepository.save(failedCommand);
        log.error("Retry failed", e);
      }
    }
  }
}
