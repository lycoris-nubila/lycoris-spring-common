package eu.lycoris.spring.cqrs;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LycorisCommandRetryService {

  private final @NotNull JobOperator jobOperator;
  private final @NotNull JobExplorer jobExplorer;
  private final @NotNull JdbcTemplate jdbcTemplate;

  public LycorisCommandRetryService(
      @NotNull JobOperator jobOperator,
      @NotNull JobExplorer jobExplorer,
      @NotNull JdbcTemplate jdbcTemplate) {
    this.jobOperator = jobOperator;
    this.jobExplorer = jobExplorer;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Scheduled(
      fixedDelayString = "${lycoris.command-retry.delay-millisec:10000}",
      initialDelayString = "${lycoris.command-retry.delay-millisec:10000}")
  public void retryFailedCommands() {
    List<Long> failedJobsId =
        this.jdbcTemplate.queryForList(
            "SELECT job_execution_id FROM batch_job_execution WHERE status='FAILED'", Long.class);

    for (Long failedJobId : failedJobsId) {
      JobExecution jobExecution = this.jobExplorer.getJobExecution(failedJobId);

      if (jobExecution == null
          || this.jobExplorer.getJobExecutions(jobExecution.getJobInstance()).size() > 4) {
        continue;
      }

      try {
        this.jobOperator.restart(failedJobId);
        this.jobOperator.abandon(failedJobId);
      } catch (JobInstanceAlreadyCompleteException
          | NoSuchJobExecutionException
          | NoSuchJobException
          | JobRestartException
          | JobParametersInvalidException
          | JobExecutionAlreadyRunningException e) {
        log.error("Can't restart command", e);
      }
    }
  }
}
