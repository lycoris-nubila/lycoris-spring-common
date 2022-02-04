package eu.lycoris.spring.ddd.command;

import lombok.*;

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

  @Id private UUID id;

  @NotNull @NotEmpty private String command;

  @NotNull @NotEmpty private String serviceClass;

  @NotNull @NotEmpty private String commmandClass;

  @NotNull @NotEmpty private String serviceMethodName;

  @Min(0)
  @NotNull
  private Integer retryCount;

  private Instant nextRetryTime;

  public void scheduleNextRetry(int maxRetry, int backoffMillisec) {
    if (retryCount < maxRetry) {
      this.nextRetryTime =
          Instant.ofEpochMilli(++retryCount * backoffMillisec + System.currentTimeMillis());
    } else {
      this.nextRetryTime = null;
    }
  }

  public static FailedCommandBuilder builder(
      String command,
      Class<? extends Command> commmandClass,
      Class<?> serviceClass,
      String serviceMethodName) {
    return new FailedCommandBuilder()
        .retryCount(0)
        .command(command)
        .id(UUID.randomUUID())
        .nextRetryTime(Instant.now())
        .serviceMethodName(serviceMethodName)
        .serviceClass(serviceClass.getCanonicalName())
        .commmandClass(commmandClass.getCanonicalName());
  }
}
