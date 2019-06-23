package eu.lycoris.spring.ddd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.lycoris.spring.ddd.command.Command;
import eu.lycoris.spring.ddd.command.FailedCommand;
import eu.lycoris.spring.ddd.command.FailedCommandRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LycorisCommandService {

  @Autowired private ObjectMapper objectMapper;

  @Autowired private FailedCommandRepository failedCommandRepository;

  public void saveCommand(
      Command command,
      Class<?> serviceClass,
      String serviceMethodName)
      throws JsonProcessingException {
    String serializedCommand = objectMapper.writeValueAsString(command);

    log.info("Saving command {}", serializedCommand);

    failedCommandRepository.save(
        FailedCommand.builder(
                serializedCommand, command.getClass(), serviceClass, serviceMethodName)
            .build());

    log.info("Saved command {}", serializedCommand);
  }
}
