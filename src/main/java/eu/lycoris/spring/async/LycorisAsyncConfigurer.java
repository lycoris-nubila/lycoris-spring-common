package eu.lycoris.spring.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class LycorisAsyncConfigurer implements AsyncConfigurer {

  @Autowired ThreadPoolTaskExecutor taskExecutor;

  @Autowired(required = false)
  private AsyncUncaughtExceptionHandler exceptionHandler;

  @Override
  public Executor getAsyncExecutor() {
    return taskExecutor;
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return exceptionHandler;
  }
}
