package eu.lycoris.spring.async;

import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class LycorisAsyncConfigurer implements AsyncConfigurer {

  @NotNull final ThreadPoolTaskExecutor taskExecutor;

  @Nullable private final AsyncUncaughtExceptionHandler exceptionHandler;

  public LycorisAsyncConfigurer(
      @NotNull ThreadPoolTaskExecutor taskExecutor,
      @Nullable @Autowired(required = false) AsyncUncaughtExceptionHandler exceptionHandler) {
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
