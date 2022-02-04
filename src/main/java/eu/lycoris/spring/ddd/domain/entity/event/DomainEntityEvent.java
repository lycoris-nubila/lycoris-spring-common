package eu.lycoris.spring.ddd.domain.entity.event;

import eu.lycoris.spring.ddd.domain.entity.IDomainEntity;
import org.springframework.context.ApplicationEvent;

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
