package eu.lycoris.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import io.leangen.graphql.spqr.spring.autoconfigure.DefaultGlobalContext;
import io.leangen.graphql.spqr.spring.autoconfigure.ServletContextFactory;

@Configuration
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
  
  @Bean
  public ServletContextFactory globalContextFactory() {
    return params -> {
      DefaultGlobalContext<?> context = new DefaultGlobalContext<>(params.getNativeRequest());
      context.setExtension("auth", SecurityContextHolder.getContext().getAuthentication());
      return context;
    };
  }
}
