package eu.lycoris.spring.ddd.domain;

import static lombok.AccessLevel.PROTECTED;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.TextType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor(access = PROTECTED)
@TypeDef(defaultForType = String.class, typeClass = TextType.class)
public class DomainEntity<T> implements IDomainEntity<T> {

  @Id private T id;

  @UpdateTimestamp private Instant updateDateTime;

  @CreationTimestamp
  @Column(updatable = false)
  private Instant creationDateTime;

  @Transient private final transient List<Object> domainEvents = new ArrayList<>();

  protected DomainEntity(T id) {
    this.id = id;
  }

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

  protected void forceUpdate() {
    this.updateDateTime = Instant.now();
  }
}
