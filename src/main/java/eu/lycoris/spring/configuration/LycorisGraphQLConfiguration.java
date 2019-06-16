package eu.lycoris.spring.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import eu.lycoris.spring.graphql.LycorisGraphQLFetchExceptionHandler;
import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.AsyncSerialExecutionStrategy;
import graphql.execution.SubscriptionExecutionStrategy;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.spqr.spring.autoconfigure.ServletContextFactory;

@Configuration
public class LycorisGraphQLConfiguration {

  @Bean
  public ServletContextFactory globalContextFactory() {
    return params -> SecurityContextHolder.getContext().getAuthentication();
  }

  @Bean
  public GraphQL graphQL(GraphQLSchema graphQLSchema, MessageSource messageSource) {
    LycorisGraphQLFetchExceptionHandler fetchExceptionHandler =
        new LycorisGraphQLFetchExceptionHandler(messageSource);
    return GraphQL.newGraphQL(graphQLSchema)
        .queryExecutionStrategy(new AsyncExecutionStrategy(fetchExceptionHandler))
        .mutationExecutionStrategy(new AsyncSerialExecutionStrategy(fetchExceptionHandler))
        .subscriptionExecutionStrategy(new SubscriptionExecutionStrategy(fetchExceptionHandler))
        .build();
  }
}
