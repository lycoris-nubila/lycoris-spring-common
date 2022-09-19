package eu.lycoris.spring.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.validation.constraints.NotNull;

@Configuration
@Profile({"!test"})
public class LycorisScheduleConfigurer implements SchedulingConfigurer {

  private final @NotNull ThreadPoolTaskScheduler taskScheduler;

  public LycorisScheduleConfigurer(@NotNull ThreadPoolTaskScheduler taskScheduler) {
    this.taskScheduler = taskScheduler;
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setTaskScheduler(this.taskScheduler);
  }
}
