package eu.lycoris.spring.configuration;

import eu.lycoris.spring.web.LycorisControllerAdvice;
import eu.lycoris.spring.web.LycorisWebMvcConfigurer;
import eu.lycoris.spring.web.LycorisWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@Import({
  LycorisWebMvcConfigurer.class,
  LycorisWebSecurityConfigurerAdapter.class,
  LycorisControllerAdvice.class
})
public class LycorisWebConfiguration {
  @Bean
  public LocaleResolver localeResolver() {
    return new SessionLocaleResolver();
  }

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor changeInterceptor = new LocaleChangeInterceptor();
    changeInterceptor.setParamName("lang");
    return changeInterceptor;
  }
}
