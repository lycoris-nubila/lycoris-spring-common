package eu.lycoris.spring.ddd;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.lycoris.spring.ddd.command.Command;
import eu.lycoris.spring.ddd.command.FailedCommand;
import eu.lycoris.spring.ddd.command.FailedCommandRepository;
import eu.lycoris.spring.ddd.service.ApplicationService;
import eu.lycoris.spring.property.LycorisProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class LycorisCommandRetryService {

  @Autowired private ObjectMapper objectMapper;

  @Autowired private LycorisProperties lycorisProperties;

  @Autowired private ListableBeanFactory listableBeanFactory;

  @Autowired private FailedCommandRepository failedCommandRepository;

  @SuppressWarnings("unchecked")
  @Scheduled(
    fixedDelayString = "${lycoris.command-retry.delay-millisec:10000}",
    initialDelayString = "${lycoris.command-retry.delay-millisec:10000}"
  )
  private void retryFailedCommands() {
    List<FailedCommand> failedCommands =
        StreamSupport.stream(failedCommandRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    for (FailedCommand failedCommand : failedCommands) {
      if (failedCommand.getNextRetryTime() == null) {
        log.info(
            "Command {} as reached maximum attempt ({})",
            failedCommand,
            lycorisProperties.getCommandRetry().getMaxAttempts());
        continue;
      } else if (failedCommand.getNextRetryTime().isAfter(Instant.now())) {
        log.info(
            "Command {} will be retried at ({})", failedCommand, failedCommand.getNextRetryTime());
        continue;
      }

      try {
        Class<? extends Command> commandClass =
            (Class<? extends Command>) Class.forName(failedCommand.getCommmandClass());
        Command command = objectMapper.readValue(failedCommand.getCommand(), commandClass);

        Class<? extends ApplicationService> serviceClass =
            (Class<? extends ApplicationService>) Class.forName(failedCommand.getServiceClass());

        Method serviceMethod =
            serviceClass.getMethod(failedCommand.getServiceMethodName(), commandClass);

        serviceMethod.invoke(listableBeanFactory.getBean(serviceClass), command);

        failedCommandRepository.delete(failedCommand);
      } catch (Exception e) {
        failedCommand.scheduleNextRetry(
            lycorisProperties.getCommandRetry().getMaxAttempts(),
            lycorisProperties.getCommandRetry().getBackoffMillisec());
        failedCommandRepository.save(failedCommand);
        log.error("Retry failed", e);
      }
    }
  }
}
