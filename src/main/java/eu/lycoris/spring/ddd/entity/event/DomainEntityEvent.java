package eu.lycoris.spring.ddd.entity.event;

import eu.lycoris.spring.ddd.entity.IDomainEntity;
import javax.validation.constraints.NotNull;
import org.springframework.context.ApplicationEvent;

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
