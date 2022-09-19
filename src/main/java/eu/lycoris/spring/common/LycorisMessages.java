package eu.lycoris.spring.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LycorisMessages {

  public static final @NotNull String ERROR_WEB_REQUEST_INTERNAL_ERROR =
      "error.web.request.internal-error";

  public static final @NotNull String ERROR_WEB_REQUEST_AUTHENTICATION_ERROR =
      "error.web.request.authentication-error";

  public static final @NotNull String ERROR_WEB_REQUEST_INVALID_BODY =
      "error.web.request.invalid-body";

  public static final @NotNull String ERROR_WEB_REQUEST_INVALID_INPUT =
      "error.web.request.invalid-input";

  public static final @NotNull String ERROR_WEB_REQUEST_INVALID_FIELD =
      "error.web.request.invalid-field";

  public static final @NotNull String ERROR_WEB_REQUEST_EXPIRED_JWT =
      "error.web.request.expired-jwt";

  public static final @NotNull String ERROR_WEB_REQUEST_INVALID_JWT =
      "error.web.request.invalid-jwt";

  public static final @NotNull String ERROR_WEB_REQUEST_CONSTRAINT_VIOLATION =
      "error.web.request.constraint-violation";
}
