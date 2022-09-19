package eu.lycoris.spring.ddd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.lycoris.spring.ddd.command.Command;
import eu.lycoris.spring.ddd.command.FailedCommand;
import eu.lycoris.spring.ddd.command.FailedCommandRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LycorisCommandService {

  private final @NotNull ObjectMapper objectMapper;

  private final @NotNull FailedCommandRepository failedCommandRepository;

  public LycorisCommandService(
      @NotNull ObjectMapper objectMapper,
      @NotNull FailedCommandRepository failedCommandRepository) {
    this.objectMapper = objectMapper;
    this.failedCommandRepository = failedCommandRepository;
  }

  public void saveCommand(
      @NotNull Command command, @NotNull Class<?> serviceClass, @NotNull String serviceMethodName)
      throws JsonProcessingException {
    String serializedCommand = this.objectMapper.writeValueAsString(command);

    log.info("Saving command {}", serializedCommand);

    this.failedCommandRepository.save(
        FailedCommand.builder(
                serializedCommand, command.getClass(), serviceClass, serviceMethodName)
            .build());

    log.info("Saved command {}", serializedCommand);
  }
}
