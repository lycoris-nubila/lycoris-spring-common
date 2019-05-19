package eu.lycoris.spring.jwt;

import lombok.Getter;
import springfox.documentation.service.ApiKey;

@Getter
public class LycorisJwtSecurityScheme extends ApiKey {

  public static final String SCHEME_NAME = "jwtAuth";

  public LycorisJwtSecurityScheme(String header) {
    super(SCHEME_NAME, header, "header");
  }
}
