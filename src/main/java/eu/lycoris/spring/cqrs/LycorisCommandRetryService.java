package eu.lycoris.spring.cqrs;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LycorisCommandRetryService {

  private final @NotNull JobExplorer jobExplorer;
  private final @NotNull JdbcTemplate jdbcTemplate;
  private final @NotNull LycorisCommandService lycorisCommandService;

  public LycorisCommandRetryService(
      @NotNull JobExplorer jobExplorer,
      @NotNull JdbcTemplate jdbcTemplate,
      @NotNull LycorisCommandService lycorisCommandService) {
    this.jobExplorer = jobExplorer;
    this.jdbcTemplate = jdbcTemplate;
    this.lycorisCommandService = lycorisCommandService;
  }

  @Scheduled(
      fixedDelayString = "${lycoris.command-retry.delay-millisec:10000}",
      initialDelayString = "${lycoris.command-retry.delay-millisec:10000}")
  public void retryFailedCommands() {
    Set<Long> jobInstancesId =
        new HashSet<>(
            this.jdbcTemplate.queryForList(
                "SELECT bi.job_instance_id "
                    + "FROM batch_job_instance bi, "
                    + "batch_job_execution be "
                    + "WHERE be.job_instance_id = bi.job_instance_id "
                    + "AND be.status = 'FAILED'",
                Long.class));

    for (Long jobInstanceId : jobInstancesId) {
      JobInstance jobInstance = this.jobExplorer.getJobInstance(jobInstanceId);
      if (jobInstance == null) {
        continue;
      }

      JobExecution lastJobExecution = this.jobExplorer.getLastJobExecution(jobInstance);
      if (lastJobExecution == null) {
        continue;
      }

      if (!lastJobExecution.getStatus().equals(BatchStatus.FAILED)
          || this.jobExplorer.getJobExecutions(jobInstance).size() > 4) {
        continue;
      }

      this.lycorisCommandService.retryCommand(lastJobExecution.getId());
    }
  }
}
