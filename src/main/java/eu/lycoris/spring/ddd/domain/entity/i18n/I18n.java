package eu.lycoris.spring.ddd.domain.entity.i18n;

import eu.lycoris.spring.ddd.domain.entity.IDomainEntity;
import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Getter
@MappedSuperclass
@Setter(PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class I18n<
    I extends Serializable, D extends IDomainEntity<I>, L extends I18n<I, D, L>> {

  @EmbeddedId
  @Getter(value = AccessLevel.PROTECTED)
  protected @NotNull I18nId id;

  public @NotNull String getLocale() {
    return this.id.getLocale();
  }

  public abstract void update(@NotNull L localization);

  protected abstract void setEntity(@NotNull I18nDomainEntity<I, D, L> domainI18nEntity);
}
