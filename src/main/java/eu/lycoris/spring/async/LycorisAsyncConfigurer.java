package eu.lycoris.spring.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.validation.constraints.NotNull;
import java.util.concurrent.Executor;

@Configuration
public class LycorisAsyncConfigurer implements AsyncConfigurer {

  private final @NotNull ThreadPoolTaskExecutor taskExecutor;

  private final @NotNull AsyncUncaughtExceptionHandler exceptionHandler;

  public LycorisAsyncConfigurer(
      @NotNull ThreadPoolTaskExecutor taskExecutor,
      @NotNull AsyncUncaughtExceptionHandler exceptionHandler) {
    this.taskExecutor = taskExecutor;
    this.exceptionHandler = exceptionHandler;
  }

  @Override
  public Executor getAsyncExecutor() {
    return this.taskExecutor;
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return this.exceptionHandler;
  }
}
