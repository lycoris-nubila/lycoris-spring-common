package eu.lycoris.spring.common;

import javax.validation.constraints.NotNull;

public interface LycorisSubjectMessage {

  @NotNull
  String getSubject();
}
