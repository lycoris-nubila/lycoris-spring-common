package eu.lycoris.spring.ddd.domain.entity;

import java.time.Instant;

public interface IDomainEntity<T> {

  T getId();

  Long getVersion();

  Instant getUpdateDateTime();

  Instant getCreationDateTime();
}
