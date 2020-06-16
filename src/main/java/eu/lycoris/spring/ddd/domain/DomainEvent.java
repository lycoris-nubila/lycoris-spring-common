package eu.lycoris.spring.ddd.domain;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class DomainEvent<I, D extends IDomainEntity<I>> extends ApplicationEvent {

  public DomainEvent(D source) {
    super(source);
  }

  @Override
  @SuppressWarnings("unchecked")
  public D getSource() {
    return (D) super.getSource();
  }
}
