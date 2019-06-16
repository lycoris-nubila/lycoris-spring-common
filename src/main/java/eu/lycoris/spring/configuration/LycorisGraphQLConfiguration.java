package eu.lycoris.spring.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import eu.lycoris.spring.graphql.LycorisGraphQLFetchExceptionHandler;
import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.schema.GraphQLSchema;
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

  @Bean
  public GraphQL graphQL(GraphQLSchema graphQLSchema, MessageSource messageSource) {
    return GraphQL.newGraphQL(graphQLSchema)
        .queryExecutionStrategy(
            new AsyncExecutionStrategy(new LycorisGraphQLFetchExceptionHandler(messageSource)))
        .build();
  }
}
