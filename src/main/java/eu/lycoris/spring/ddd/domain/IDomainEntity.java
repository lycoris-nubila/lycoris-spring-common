package eu.lycoris.spring.ddd.domain;

import java.time.Instant;

public interface IDomainEntity<T> {
	
	public T getId();
	public Instant getUpdateDateTime();
	public Instant getCreationDateTime();
	
}
