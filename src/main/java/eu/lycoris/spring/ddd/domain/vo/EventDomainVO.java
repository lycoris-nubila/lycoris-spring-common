package eu.lycoris.spring.ddd.domain.vo;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
public class EventDomainVO<I extends Serializable, D extends IDomainVO<I>> {

  @Transient private final @NotNull List<DomainVOEvent<I>> domainEvents = new ArrayList<>();

  @AfterDomainEventPublication
  protected void clearDomainEvents() {
    this.domainEvents.clear();
  }

  @DomainEvents
  protected @NotNull Collection<Object> domainEvents() {
    return Collections.unmodifiableList(this.domainEvents);
  }

  protected @NotNull DomainVOEvent<I> registerEvent(@NotNull DomainVOEvent<I> event) {
    Assert.notNull(event, "Domain event must not be null!");
    this.domainEvents.add(event);
    return event;
  }
}
