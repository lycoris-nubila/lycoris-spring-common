package eu.lycoris.spring.ddd.domain.vo;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

public interface IDomainVO<T> {

  @NotNull
  T getId();

  @Nullable
  Long getVersion();
}
