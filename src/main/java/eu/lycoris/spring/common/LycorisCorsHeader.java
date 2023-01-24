package eu.lycoris.spring.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LycorisCorsHeader {

  private String name;
  private boolean exposed;
  private boolean allowed;
}
