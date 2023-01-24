package eu.lycoris.spring.ddd.entity.event;

import static lombok.AccessLevel.PROTECTED;

import eu.lycoris.spring.ddd.entity.IDomainEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;

@NoArgsConstructor(access = PROTECTED)
public class EventDomainEntity<I, D extends IDomainEntity<I>> {

  @Transient private final @NotNull List<DomainEntityEvent<I, D>> domainEvents = new ArrayList<>();

  @AfterDomainEventPublication
  protected void clearDomainEvents() {
    this.domainEvents.clear();
  }

  @DomainEvents
  protected @NotNull Collection<Object> domainEvents() {
    return Collections.unmodifiableList(this.domainEvents);
  }

  protected @NotNull DomainEntityEvent<I, D> registerEvent(@NotNull DomainEntityEvent<I, D> event) {
    Assert.notNull(event, "Domain event must not be null!");
    this.domainEvents.add(event);
    return event;
  }
}
