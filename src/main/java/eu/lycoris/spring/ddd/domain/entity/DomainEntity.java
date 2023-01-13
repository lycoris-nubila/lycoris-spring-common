package eu.lycoris.spring.ddd.domain.entity;

import eu.lycoris.spring.ddd.domain.entity.event.EventDomainEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.TextType;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

import static lombok.AccessLevel.PROTECTED;

@Getter
@MappedSuperclass
@NoArgsConstructor
@RequiredArgsConstructor(access = PROTECTED)
@TypeDef(defaultForType = String.class, typeClass = TextType.class)
public class DomainEntity<I extends Serializable, D extends IDomainEntity<I>> extends EventDomainEntity<I, D>
    implements IDomainEntity<I> {

  @Id @NotNull @NonNull private I id;

  @Version
  @Column(columnDefinition = "bigint default 0")
  private Long version;

  @UpdateTimestamp private Instant updateDateTime;

  @CreationTimestamp
  @Column(updatable = false)
  private Instant creationDateTime;

  protected void forceUpdate() {
    this.updateDateTime = Instant.now();
  }
}
