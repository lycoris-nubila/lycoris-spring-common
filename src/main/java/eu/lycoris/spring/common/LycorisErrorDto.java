package eu.lycoris.spring.common;

import lombok.*;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Builder
@Getter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LycorisErrorDto {
  private @NotNull String message;

  private @Nullable List<String> errorList;

  private @Nullable Map<String, String> fieldErrorList;

  public static @NotNull LycorisErrorDtoBuilder builder(@NotNull String message) {
    return new LycorisErrorDtoBuilder().message(message);
  }

  public @Nullable List<String> getErrorList() {
    return this.errorList;
  }

  public @Nullable Map<String, String> getFieldErrorList() {
    return this.fieldErrorList;
  }

  public @NotNull String getMessage() {
    return this.message;
  }
}
