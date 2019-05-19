package eu.lycoris.spring.ddd.domain;

public interface DomainCommand {

  public default Boolean isRetryable() {
    return true;
  }
}
