package eu.lycoris.spring.ddd.domain.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.TypeDef;
import org.hibernate.type.TextType;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Getter
@MappedSuperclass
@NoArgsConstructor
@RequiredArgsConstructor(access = PROTECTED)
@TypeDef(defaultForType = String.class, typeClass = TextType.class)
public class DomainVO<I extends Serializable, D extends IDomainVO<I>> extends EventDomainVO<I, D>
    implements IDomainVO<I> {

  @Id @NotNull @NonNull private I id;

  @Version
  @Nullable
  @Column(columnDefinition = "bigint default 0")
  private Long version;
}
