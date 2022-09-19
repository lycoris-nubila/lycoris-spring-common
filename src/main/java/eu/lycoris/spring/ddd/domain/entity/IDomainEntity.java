package eu.lycoris.spring.ddd.domain.entity;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.time.Instant;

public interface IDomainEntity<I> {

  @NotNull
  I getId();

  @Nullable
  Long getVersion();

  @Nullable
  Instant getUpdateDateTime();

  @Nullable
  Instant getCreationDateTime();
}
