package eu.lycoris.spring.ddd.domain.entity.event;

import eu.lycoris.spring.ddd.domain.entity.IDomainEntity;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
public class EventDomainEntity<I, D extends IDomainEntity<I>> {

  @Transient private final @NotNull List<DomainEntityEvent<I>> domainEvents = new ArrayList<>();

  @AfterDomainEventPublication
  protected void clearDomainEvents() {
    this.domainEvents.clear();
  }

  @DomainEvents
  protected @NotNull Collection<Object> domainEvents() {
    return Collections.unmodifiableList(this.domainEvents);
  }

  protected @NotNull DomainEntityEvent<I> registerEvent(@NotNull DomainEntityEvent<I> event) {
    Assert.notNull(event, "Domain event must not be null!");
    this.domainEvents.add(event);
    return event;
  }
}
