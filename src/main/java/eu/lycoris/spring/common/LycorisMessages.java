package eu.lycoris.spring.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LycorisMessages {

  public static final String ERROR_WEB_REQUEST_INTERNAL_ERROR = "error.web.request.internal-error";

  public static final String ERROR_WEB_REQUEST_AUTHENTICATION_ERROR =
      "error.web.request.authentication-error";

  public static final String ERROR_WEB_REQUEST_INVALID_BODY = "error.web.request.invalid-body";

  public static final String ERROR_WEB_REQUEST_INVALID_INPUT = "error.web.request.invalid-input";

  public static final String ERROR_WEB_REQUEST_INVALID_FIELD = "error.web.request.invalid-field";

  public static final String ERROR_WEB_REQUEST_EXPIRED_JWT = "error.web.request.expired-jwt";

  public static final String ERROR_WEB_REQUEST_INVALID_JWT = "error.web.request.invalid-jwt";
}
