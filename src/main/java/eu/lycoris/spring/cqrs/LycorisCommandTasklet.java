package eu.lycoris.spring.cqrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.lycoris.spring.cqrs.model.Command;
import lombok.Builder;
import lombok.Value;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.ListableBeanFactory;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

@Value
@Builder
public class LycorisCommandTasklet implements Tasklet {

  @NotNull ObjectMapper objectMapper;

  @NotNull ListableBeanFactory listableBeanFactory;

  public static @NotNull LycorisCommandTaskletBuilder builder(
      @NotNull ObjectMapper objectMapper, @NotNull ListableBeanFactory listableBeanFactory) {
    return new LycorisCommandTaskletBuilder()
        .listableBeanFactory(listableBeanFactory)
        .objectMapper(objectMapper);
  }

  @Override
  public RepeatStatus execute(
      @NotNull StepContribution contribution, @NotNull ChunkContext chunkContext) throws Exception {
    Command command =
        this.objectMapper.readValue(
            contribution.getStepExecution().getJobParameters().getString("command"), Command.class);

    if (command.getServiceClass() == null || command.getMethodName() == null) {
      throw new IllegalArgumentException("No service class or method name");
    }

    Method serviceMethod =
        command.getServiceClass().getMethod(command.getMethodName(), command.getClass());

    serviceMethod.invoke(this.listableBeanFactory.getBean(command.getServiceClass()), command);

    return RepeatStatus.FINISHED;
  }
}
