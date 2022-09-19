package eu.lycoris.spring.common;

import javax.validation.constraints.NotNull;

public class LycorisAuthenticationException extends LycorisApplicationException {

  public LycorisAuthenticationException(@NotNull String messageKey) {
    super(messageKey);
  }

  public LycorisAuthenticationException(@NotNull String messageKey, @NotNull Throwable cause) {
    super(messageKey, cause);
  }
}
