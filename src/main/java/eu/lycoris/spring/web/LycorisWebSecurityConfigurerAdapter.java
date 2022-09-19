package eu.lycoris.spring.web;

import eu.lycoris.spring.common.LycorisCorsHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@EnableWebSecurity
public class LycorisWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter
    implements CorsConfigurationSource {

  private final @Nullable List<LycorisCorsHeader> corsHeaders;

  public LycorisWebSecurityConfigurerAdapter(@Nullable List<LycorisCorsHeader> corsHeaders) {
    this.corsHeaders = corsHeaders;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .cors()
        .configurationSource(this)
        .and()
        .headers()
        .defaultsDisabled()
        .cacheControl();
  }

  @NotNull
  @Override
  public CorsConfiguration getCorsConfiguration(@NotNull HttpServletRequest request) {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addExposedHeader(CONTENT_DISPOSITION);
    configuration.addAllowedMethod(HttpMethod.OPTIONS);
    configuration.addAllowedMethod(HttpMethod.DELETE);
    configuration.addAllowedMethod(HttpMethod.PATCH);
    configuration.addAllowedMethod(HttpMethod.POST);
    configuration.addAllowedMethod(HttpMethod.PUT);
    configuration.addAllowedMethod(HttpMethod.GET);
    configuration.addAllowedHeader(CONTENT_TYPE);
    configuration.addAllowedOrigin("*");

    if (this.corsHeaders != null) {
      this.corsHeaders.stream()
          .filter(LycorisCorsHeader::isAllowed)
          .map(LycorisCorsHeader::getName)
          .forEach(configuration::addAllowedHeader);
      this.corsHeaders.stream()
          .filter(LycorisCorsHeader::isExposed)
          .map(LycorisCorsHeader::getName)
          .forEach(configuration::addExposedHeader);
    }

    return configuration;
  }
}
