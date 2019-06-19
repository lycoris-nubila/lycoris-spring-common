package eu.lycoris.spring.ddd;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import eu.lycoris.spring.ddd.command.Command;

@Component
public class LycorisExceptionHandler implements AsyncUncaughtExceptionHandler {

  @Autowired @Lazy private LycorisCommandBus lycorisCommandBus;

  @Override
  public void handleUncaughtException(Throwable ex, Method method, Object... params) {
    if (params.length > 0 && params[0] instanceof Command) {
      Command command = (Command) params[0];
      lycorisCommandBus.handleError(command, ex);
    }
  }
}
