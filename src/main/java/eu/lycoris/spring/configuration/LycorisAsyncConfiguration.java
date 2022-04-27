package eu.lycoris.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@EnableAsync
@Configuration
@EnableScheduling
public class LycorisAsyncConfiguration {

  @Bean
  @Primary
  public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setThreadNamePrefix("Lycoris-Async-");
    executor.setQueueCapacity(Integer.MAX_VALUE);
    executor.setAwaitTerminationSeconds(30);
    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(10);
    executor.initialize();
    return executor;
  }

  @Bean
  public ThreadPoolTaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setThreadNamePrefix("Lycoris-Schedule-");
    threadPoolTaskScheduler.setAwaitTerminationSeconds(30);
    threadPoolTaskScheduler.setPoolSize(10);
    threadPoolTaskScheduler.initialize();
    return threadPoolTaskScheduler;
  }
}
