package eu.lycoris.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import io.leangen.graphql.spqr.spring.autoconfigure.DefaultGlobalContext;
import io.leangen.graphql.spqr.spring.autoconfigure.ServletContextFactory;

@Configuration
public class LycorisGraphQLConfiguration {

  @Bean
  public ServletContextFactory globalContextFactory() {
    return params -> {
      DefaultGlobalContext<?> context = new DefaultGlobalContext<>(params.getNativeRequest());
      context.setExtension("auth", SecurityContextHolder.getContext().getAuthentication());
      return context;
    };
  }
}
