package eu.lycoris.spring.ddd.entity.event;

import eu.lycoris.spring.ddd.entity.IDomainEntity;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.NotNull;

public class DomainEntityEvent<I, D extends IDomainEntity<I>> extends ApplicationEvent {

  public DomainEntityEvent(@NotNull D source) {
    super(source);
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull D getSource() {
    return (D) super.getSource();
  }
}
