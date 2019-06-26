package eu.lycoris.spring.common;

@SuppressWarnings("serial")
public class LycorisAuthenticationException extends LycorisApplicationException {

  public LycorisAuthenticationException(String messageKey) {
    super(messageKey);
  }

  public LycorisAuthenticationException(String messageKey, Throwable cause) {
    super(messageKey, cause);
  }
}
