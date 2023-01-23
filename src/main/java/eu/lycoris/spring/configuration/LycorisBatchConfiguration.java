package eu.lycoris.spring.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.lycoris.spring.cqrs.LycorisCommandTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Configuration
@EnableBatchProcessing
public class LycorisBatchConfiguration {
  private final @NotNull JobBuilderFactory jobs;
  private final @NotNull StepBuilderFactory steps;
  private final @NotNull ObjectMapper objectMapper;
  private final @NotNull ListableBeanFactory listableBeanFactory;

  public LycorisBatchConfiguration(
      @NotNull StepBuilderFactory steps,
      @NotNull JobBuilderFactory jobs,
      @NotNull ObjectMapper objectMapper,
      @NotNull ListableBeanFactory listableBeanFactory) {
    this.listableBeanFactory = listableBeanFactory;
    this.objectMapper = objectMapper;
    this.steps = steps;
    this.jobs = jobs;
  }

  @Bean
  public @NotNull Step commandStep() {
    return this.steps
        .get("commandStep")
        .tasklet(LycorisCommandTasklet.builder(this.objectMapper, this.listableBeanFactory).build())
        .allowStartIfComplete(true)
        .build();
  }

  @Bean
  public @NotNull Job commandJob(@Qualifier("commandStep") @NotNull Step commandStep) {
    return this.jobs.get("commandJob").start(commandStep).build();
  }

  @Bean
  public @NotNull JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(
      @NotNull JobRegistry jobRegistry) {
    final JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor =
        new JobRegistryBeanPostProcessor();
    jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
    return jobRegistryBeanPostProcessor;
  }
}
