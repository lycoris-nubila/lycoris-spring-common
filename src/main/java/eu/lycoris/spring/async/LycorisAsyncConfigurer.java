package eu.lycoris.spring.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Nullable;
import java.util.concurrent.Executor;

@Configuration
public class LycorisAsyncConfigurer implements AsyncConfigurer {

    private final @Nullable ThreadPoolTaskExecutor taskExecutor;

    private final @Nullable AsyncUncaughtExceptionHandler exceptionHandler;

    public LycorisAsyncConfigurer(
            @Nullable @Autowired(required = false) ThreadPoolTaskExecutor taskExecutor,
            @Nullable @Autowired(required = false)
            AsyncUncaughtExceptionHandler exceptionHandler) {
        this.taskExecutor = taskExecutor;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    @Nullable
    public Executor getAsyncExecutor() {
        return this.taskExecutor;
    }

    @Override
    @Nullable
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return this.exceptionHandler;
    }
}
