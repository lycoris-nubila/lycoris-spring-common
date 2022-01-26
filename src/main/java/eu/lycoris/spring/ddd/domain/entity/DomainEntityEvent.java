package eu.lycoris.spring.ddd.domain.entity;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class DomainEntityEvent<I, D extends IDomainEntity<I>> extends ApplicationEvent {

  public DomainEntityEvent(D source) {
    super(source);
  }

  @Override
  @SuppressWarnings("unchecked")
  public D getSource() {
    return (D) super.getSource();
  }
}
