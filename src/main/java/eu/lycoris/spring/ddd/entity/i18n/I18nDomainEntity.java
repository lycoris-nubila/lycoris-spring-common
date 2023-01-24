package eu.lycoris.spring.ddd.entity.i18n;

import static lombok.AccessLevel.PROTECTED;

import eu.lycoris.spring.ddd.entity.DomainEntity;
import eu.lycoris.spring.ddd.entity.IDomainEntity;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@MappedSuperclass
@Setter(PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public abstract class I18nDomainEntity<
        I extends Serializable, D extends IDomainEntity<I>, L extends I18n<I, D, L>>
    extends DomainEntity<I, D> {

  @Transient
  protected abstract List<L> getMutableLocalizations();

  protected void updateLocalizations(List<L> localizations) {
    Map<String, L> localizationsMap =
        this.getMutableLocalizations().stream()
            .collect(Collectors.toMap(L::getLocale, Function.identity()));
    this.getMutableLocalizations().clear();

    for (L localization : localizations) {
      L finalLocalization = localizationsMap.getOrDefault(localization.getLocale(), localization);
      finalLocalization.update(localization);
      finalLocalization.setEntity(this);
      this.getMutableLocalizations().add(finalLocalization);
    }

    this.forceUpdate();
  }

  public I18nDomainEntity(I id) {
    super(id);
  }
}
