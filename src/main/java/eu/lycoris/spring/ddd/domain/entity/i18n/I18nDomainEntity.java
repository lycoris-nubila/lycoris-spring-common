package eu.lycoris.spring.ddd.domain.entity.i18n;

import eu.lycoris.spring.ddd.domain.entity.DomainEntity;
import eu.lycoris.spring.ddd.domain.entity.IDomainEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@Getter
@MappedSuperclass
@Setter(PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public abstract class I18nDomainEntity<I, D extends IDomainEntity<I>, L extends I18n<I, D, L>>
    extends DomainEntity<I, D> {

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
}