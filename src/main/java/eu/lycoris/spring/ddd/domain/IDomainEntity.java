package eu.lycoris.spring.ddd.domain;

import java.time.Instant;

public interface IDomainEntity<T> {

  T getId();

  Long getVersion();

  Instant getUpdateDateTime();

  Instant getCreationDateTime();
}
