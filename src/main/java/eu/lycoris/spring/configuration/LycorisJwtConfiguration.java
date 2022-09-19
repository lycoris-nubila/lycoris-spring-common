package eu.lycoris.spring.configuration;

import eu.lycoris.spring.common.LycorisCorsHeader;
import eu.lycoris.spring.jwt.LycorisJwtHandlerInterceptor;
import eu.lycoris.spring.jwt.LycorisJwtSecretProvider;
import eu.lycoris.spring.jwt.LycorisJwtSecurityScheme;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;
import springfox.documentation.service.SecurityScheme;

import javax.validation.constraints.NotNull;

@Configuration
public class LycorisJwtConfiguration {

  @Bean
  public @NotNull LycorisCorsHeader authHeader() {
    return new LycorisCorsHeader(HttpHeaders.AUTHORIZATION, true, true);
  }

  @Bean
  public @NotNull HandlerInterceptor interceptors(
      @NotNull LycorisJwtSecretProvider jwtSecret, @NotNull MessageSource messageSource) {
    return new LycorisJwtHandlerInterceptor(jwtSecret, messageSource);
  }

  @Bean
  @ConditionalOnMissingBean({SecurityScheme.class})
  public @NotNull LycorisJwtSecurityScheme jwtSecurityScheme() {
    return new LycorisJwtSecurityScheme(HttpHeaders.AUTHORIZATION);
  }
}
