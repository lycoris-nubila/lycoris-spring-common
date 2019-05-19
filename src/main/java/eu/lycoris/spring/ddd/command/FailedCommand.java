package eu.lycoris.spring.ddd.command;

import static lombok.AccessLevel.PROTECTED;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import eu.lycoris.spring.ddd.domain.DomainCommand;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class FailedCommand {

  @Id private UUID id;

  @NotNull @NotEmpty private String command;

  @NotNull @NotEmpty private String commmandClass;

  @Min(0)
  @NotNull
  private Integer retryCount;

  private Instant nextRetryTime;

  @Builder
  protected FailedCommand(String command, String commmandClass) {
    this.retryCount = 0;
    this.command = command;
    this.id = UUID.randomUUID();
    this.commmandClass = commmandClass;
    this.nextRetryTime = Instant.now();
  }

  public void scheduleNextRetry(int maxRetry, int backoffMillisec) {
    if (retryCount < maxRetry) {
      this.nextRetryTime =
          Instant.ofEpochMilli(++retryCount * backoffMillisec + System.currentTimeMillis());
    } else {
      this.nextRetryTime = null;
    }
  }

  public static FailedCommandBuilder builder(
      String command, Class<? extends DomainCommand> commmandClass) {
    return new FailedCommandBuilder()
        .command(command)
        .commmandClass(commmandClass.getCanonicalName());
  }
}
