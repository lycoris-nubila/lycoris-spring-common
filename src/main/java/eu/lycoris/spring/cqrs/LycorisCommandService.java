package eu.lycoris.spring.cqrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.lycoris.spring.cqrs.model.Command;
import javax.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class LycorisCommandService {

  private final @NotNull Job commandJob;
  private final @NotNull ObjectMapper objectMapper;
  private final @NotNull JobRepository jobRepository;

  public LycorisCommandService(
      @Qualifier("commandJob") @NotNull Job commandJob,
      @NotNull ObjectMapper objectMapper,
      @NotNull JobRepository jobRepository) {
    this.commandJob = commandJob;
    this.objectMapper = objectMapper;
    this.jobRepository = jobRepository;
  }

  @SneakyThrows
  public void saveCommand(@NotNull Command command) {
    if (command.getId() == null) {
      return;
    }

    JobParameters parameters =
        new JobParametersBuilder()
            .addString("command", this.objectMapper.writeValueAsString(command))
            .addString("id", command.getId().toString())
            .toJobParameters();

    if (this.jobRepository.isJobInstanceExists(this.commandJob.getName(), parameters)) {
      return;
    }

    JobExecution execution =
        this.jobRepository.createJobExecution(this.commandJob.getName(), parameters);
    execution.upgradeStatus(BatchStatus.FAILED);
    this.jobRepository.update(execution);
  }
}
