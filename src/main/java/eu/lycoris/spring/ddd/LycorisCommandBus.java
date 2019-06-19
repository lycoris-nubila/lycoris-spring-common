package eu.lycoris.spring.ddd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.lycoris.spring.ddd.command.Command;
import eu.lycoris.spring.ddd.command.FailedCommand;
import eu.lycoris.spring.ddd.command.FailedCommandRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LycorisCommandBus {

  @Value("${command.retry.backoff:15000}")
  private Integer retryBackoffMillisec;

  @Value("${command.retry.maxAttempts:4}")
  private Integer retryMaxAttempts;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private ThreadPoolTaskExecutor taskExecutor;

  @Autowired private ListableBeanFactory listableBeanFactory;

  @Autowired private FailedCommandRepository failedCommandRepository;

  public <R extends Command, T> T run(R command) {
    return this.run(command, true);
  }

  public <R extends Command, T> CompletableFuture<T> runAsync(R command) {
    return CompletableFuture.supplyAsync(() -> this.run(command), taskExecutor);
  }

  @SuppressWarnings("unchecked")
  private <R extends Command, T> T run(R command, boolean handleError) {
    try {
      List<Object> validBeans =
          listableBeanFactory
              .getBeansOfType(Object.class)
              .values()
              .stream()
              .filter(
                  bean ->
                      Arrays.asList(bean.getClass().getMethods())
                          .stream()
                          .anyMatch(
                              m ->
                                  Arrays.asList(m.getParameterTypes())
                                      .contains(command.getClass())))
              .collect(Collectors.toList());

      if (validBeans.isEmpty()) {
        throw new IllegalArgumentException(
            String.format(
                "No bean with method having parameter '%1$s'",
                command.getClass().getCanonicalName()));
      } else if (validBeans.size() > 1) {
        throw new IllegalArgumentException(
            String.format(
                "Multiple beans with method having parameter '%1$s'",
                command.getClass().getCanonicalName()));
      }

      Object validBean = validBeans.get(0);

      List<Method> validMethods =
          Arrays.asList(validBean.getClass().getMethods())
              .stream()
              .filter(m -> Arrays.asList(m.getParameterTypes()).contains(command.getClass()))
              .collect(Collectors.toList());

      if (validMethods.isEmpty()) {
        throw new IllegalArgumentException(
            String.format(
                "No method having parameter '%1$s' in '%2$s'",
                command.getClass().getCanonicalName(), validBean.getClass().getCanonicalName()));
      } else if (validMethods.size() > 1) {
        throw new IllegalArgumentException(
            String.format(
                "Multiple methods having parameter '%1$s' in '%2$s'",
                command.getClass().getCanonicalName(), validBean.getClass().getCanonicalName()));
      }

      return (T) validMethods.get(0).invoke(validBean, command);
    } catch (InvocationTargetException e) {
      if (handleError) {
        this.handleError(command, e.getTargetException());
      }
      throw new CompletionException(e.getTargetException());
    } catch (Exception e) {
      if (handleError) {
        this.handleError(command, e);
      }
      throw new CompletionException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Scheduled(
    fixedDelayString = "${command.retry.delay:10000}",
    initialDelayString = "${command.retry.delay:10000}"
  )
  private void retryFailedCommands() {
    List<FailedCommand> failedCommands =
        StreamSupport.stream(failedCommandRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    for (FailedCommand failedCommand : failedCommands) {
      if (failedCommand.getNextRetryTime() == null
          || failedCommand.getNextRetryTime().isAfter(Instant.now())) {
        continue;
      }

      try {
        Class<? extends Command> clazz =
            (Class<? extends Command>) Class.forName(failedCommand.getCommmandClass());
        Command command = objectMapper.readValue(failedCommand.getCommand(), clazz);

        this.run(command, false);

        failedCommandRepository.delete(failedCommand);
      } catch (Exception e) {
        failedCommand.scheduleNextRetry(retryMaxAttempts, retryBackoffMillisec);
        failedCommandRepository.save(failedCommand);
      }
    }
  }

  <R extends Command> void handleError(R command, Throwable error) {
    if (error != null && BooleanUtils.isTrue(command.isRetryable())) {
      try {
        failedCommandRepository.save(
            FailedCommand.builder(objectMapper.writeValueAsString(command), command.getClass())
                .build());
        log.info("Saved failed command {}", command);
      } catch (JsonProcessingException e) {
        throw new CompletionException(e);
      }
    }
  }
}
