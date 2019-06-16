package eu.lycoris.spring.common;

@SuppressWarnings("serial")
public class LycorisApplicationException extends RuntimeException {

  public LycorisApplicationException(String messageKey, Throwable cause) {
    super(messageKey, cause);
  }

  public LycorisApplicationException(String messageKey) {
    super(messageKey);
  }
}
