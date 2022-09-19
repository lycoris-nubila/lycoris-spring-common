package eu.lycoris.spring.jwt;

import javax.validation.constraints.NotNull;

public interface LycorisJwtSecretProvider {

  @NotNull
  String getSecret();
}
