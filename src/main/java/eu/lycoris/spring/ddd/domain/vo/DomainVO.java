package eu.lycoris.spring.ddd.domain.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.TypeDef;
import org.hibernate.type.TextType;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PROTECTED;

@Getter
@MappedSuperclass
@NoArgsConstructor
@RequiredArgsConstructor(access = PROTECTED)
@TypeDef(defaultForType = String.class, typeClass = TextType.class)
public class DomainVO<I, D extends IDomainVO<I>> extends EventDomainVO<I, D>
    implements IDomainVO<I> {

  @Id @NotNull @NonNull private I id;

  @Version private Long version;
}
