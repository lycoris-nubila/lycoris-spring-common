package eu.lycoris.spring.jwt;

import lombok.Getter;
import springfox.documentation.service.ApiKey;

import javax.validation.constraints.NotNull;

@Getter
public class LycorisJwtSecurityScheme extends ApiKey {

  public static final @NotNull String SCHEME_NAME = "jwtAuth";

  public LycorisJwtSecurityScheme(@NotNull String header) {
    super(SCHEME_NAME, header, "header");
  }
}
