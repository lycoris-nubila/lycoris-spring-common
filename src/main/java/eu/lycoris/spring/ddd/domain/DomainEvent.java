package eu.lycoris.spring.ddd.domain;

import org.springframework.context.ApplicationEvent;

public class DomainEvent<U, T extends IDomainEntity<U>> extends ApplicationEvent {

  private static final long serialVersionUID = 3063562530901126531L;

  public DomainEvent(T source) {
    super(source);
  }
}
