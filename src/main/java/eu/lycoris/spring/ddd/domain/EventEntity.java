package eu.lycoris.spring.ddd.domain;

import static lombok.AccessLevel.PROTECTED;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
public class EventEntity<I> {

  @Transient private final List<Object> domainEvents = new ArrayList<>();

  @AfterDomainEventPublication
  protected void clearDomainEvents() {
    this.domainEvents.clear();
  }

  @DomainEvents
  protected Collection<Object> domainEvents() {
    return Collections.unmodifiableList(domainEvents);
  }

  protected <E> E registerEvent(E event) {
    Assert.notNull(event, "Domain event must not be null!");
    this.domainEvents.add(event);
    return event;
  }
}
