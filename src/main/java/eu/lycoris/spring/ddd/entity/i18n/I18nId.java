package eu.lycoris.spring.ddd.entity.i18n;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.BatchSize;

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
