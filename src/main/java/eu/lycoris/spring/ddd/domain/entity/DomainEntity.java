package eu.lycoris.spring.ddd.domain.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.TextType;

import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@RequiredArgsConstructor(access = PROTECTED)
@TypeDef(defaultForType = String.class, typeClass = TextType.class)
public class DomainEntity<I, D extends IDomainEntity<I>> extends EventDomainEntity<I, D>
    implements IDomainEntity<I> {

  @Id @NotNull @NonNull private I id;

  @Version private Long version;

  @UpdateTimestamp private Instant updateDateTime;

  @CreationTimestamp
  @Column(updatable = false)
  private Instant creationDateTime;

  protected void forceUpdate() {
    this.updateDateTime = Instant.now();
  }
}
