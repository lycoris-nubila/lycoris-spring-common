package eu.lycoris.spring.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import eu.lycoris.spring.common.LycorisCorsHeader;
import eu.lycoris.spring.jwt.LycorisJwtHandlerInterceptor;
import eu.lycoris.spring.jwt.LycorisJwtSecretProvider;
import eu.lycoris.spring.jwt.LycorisJwtSecurityScheme;
import springfox.documentation.service.SecurityScheme;

@Configuration
public class LycorisJwtConfiguration {

  @Bean
  public LycorisCorsHeader authHeader() {
    return new LycorisCorsHeader(HttpHeaders.AUTHORIZATION, true, true);
  }

  @Bean
  public HandlerInterceptor interceptors(
      LycorisJwtSecretProvider jwtSecret, MessageSource messageSource) {
    return new LycorisJwtHandlerInterceptor(jwtSecret, messageSource);
  }

  @Bean
  @ConditionalOnMissingBean({SecurityScheme.class})
  public LycorisJwtSecurityScheme jwtSecurityScheme() {
    return new LycorisJwtSecurityScheme(HttpHeaders.AUTHORIZATION);
  }
}
