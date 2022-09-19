package eu.lycoris.spring.common;

import javax.validation.constraints.NotNull;

public class LycorisApplicationException extends RuntimeException {

  public LycorisApplicationException(@NotNull String messageKey, @NotNull Throwable cause) {
    super(messageKey, cause);
  }

  public LycorisApplicationException(@NotNull String messageKey) {
    super(messageKey);
  }
}
