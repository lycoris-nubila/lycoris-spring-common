package eu.lycoris.spring.ddd.domain;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class DomainEvent<U, T extends IDomainEntity<U>> extends ApplicationEvent {

  public DomainEvent(T source) {
    super(source);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T getSource() {
    return (T) super.getSource();
  }
}
