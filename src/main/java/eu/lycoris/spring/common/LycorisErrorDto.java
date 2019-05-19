package eu.lycoris.spring.common;

import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LycorisErrorDto {
  private String message;

  private List<String> errorList;

  private Map<String, String> fieldErrorList;

  public static LycorisErrorDtoBuilder builder(String message) {
    return new LycorisErrorDtoBuilder().message(message);
  }

  public List<String> getErrorList() {
    return errorList;
  }

  public Map<String, String> getFieldErrorList() {
    return fieldErrorList;
  }

  public String getMessage() {
    return message;
  }
}
