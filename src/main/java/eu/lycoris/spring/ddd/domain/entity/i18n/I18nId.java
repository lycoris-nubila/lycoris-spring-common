package eu.lycoris.spring.ddd.domain.entity.i18n;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@BatchSize(size = 100)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class I18nId implements Serializable {

  private UUID id;

  private String locale;

  public I18nId(String locale) {
    this.locale = locale;
  }
}
