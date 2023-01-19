package eu.lycoris.spring.ddd.entity;

import java.time.Instant;

public interface IDomainEntity<I> {

  I getId();

  Long getVersion();

  Instant getUpdateDateTime();

  Instant getCreationDateTime();
}
