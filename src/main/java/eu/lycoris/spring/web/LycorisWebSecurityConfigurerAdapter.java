package eu.lycoris.spring.web;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import eu.lycoris.spring.common.LycorisCorsHeader;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@EnableWebSecurity
public class LycorisWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter
    implements CorsConfigurationSource {

  @Autowired(required = false)
  private List<LycorisCorsHeader> corsHeaders;

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

  @Override
  public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
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

    if (corsHeaders != null) {
      corsHeaders.stream()
          .filter(LycorisCorsHeader::isAllowed)
          .map(LycorisCorsHeader::getName)
          .forEach(configuration::addAllowedHeader);
      corsHeaders.stream()
          .filter(LycorisCorsHeader::isExposed)
          .map(LycorisCorsHeader::getName)
          .forEach(configuration::addExposedHeader);
    }

    return configuration;
  }
}
