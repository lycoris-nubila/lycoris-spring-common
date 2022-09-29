package eu.lycoris.spring.ddd.command;

import lombok.*;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class FailedCommand {

  @Id private @NotNull UUID id;

  @NotNull @NotEmpty private String command;

  @NotNull @NotEmpty private String serviceClass;

  @NotNull @NotEmpty private String commmandClass;

  @NotNull @NotEmpty private String serviceMethodName;

  @Min(0)
  @NotNull
  private Integer retryCount;

  private @Nullable Instant nextRetryTime;

  public void scheduleNextRetry(int maxRetry, int backoffMillisecond) {
    if (this.retryCount < maxRetry) {
      this.nextRetryTime =
          Instant.ofEpochMilli(
              (long) ++this.retryCount * backoffMillisecond + System.currentTimeMillis());
    } else {
      this.nextRetryTime = null;
    }
  }

  public static @NotNull FailedCommandBuilder builder(
      @NotNull String command,
      @NotNull Class<? extends Command> commandClass,
      @NotNull Class<?> serviceClass,
      @NotNull String serviceMethodName) {
    return new FailedCommandBuilder()
        .retryCount(0)
        .command(command)
        .id(UUID.randomUUID())
        .nextRetryTime(Instant.now())
        .serviceMethodName(serviceMethodName)
        .serviceClass(serviceClass.getCanonicalName())
        .commmandClass(commandClass.getCanonicalName());
  }
}
