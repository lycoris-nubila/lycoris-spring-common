package eu.lycoris.spring.ddd.command;

public interface Command {

  public default Boolean isRetryable() {
    return true;
  }
}
