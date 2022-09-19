package eu.lycoris.spring.configuration;

import eu.lycoris.spring.common.LycorisGraphQLContext;
import eu.lycoris.spring.graphql.LycorisGraphQLFetchExceptionHandler;
import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.AsyncSerialExecutionStrategy;
import graphql.execution.SubscriptionExecutionStrategy;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.spqr.spring.autoconfigure.MvcContextFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.constraints.NotNull;

@Configuration
public class LycorisGraphQLConfiguration {

  @Bean
  public @NotNull MvcContextFactory globalContextFactory() {
    return params ->
        new LycorisGraphQLContext(
            SecurityContextHolder.getContext().getAuthentication(), params.getNativeRequest());
  }

  @Bean
  public @NotNull GraphQL graphQL(
      @NotNull GraphQLSchema graphQLSchema, @NotNull MessageSource messageSource) {
    LycorisGraphQLFetchExceptionHandler fetchExceptionHandler =
        new LycorisGraphQLFetchExceptionHandler(messageSource);
    return GraphQL.newGraphQL(graphQLSchema)
        .queryExecutionStrategy(new AsyncExecutionStrategy(fetchExceptionHandler))
        .mutationExecutionStrategy(new AsyncSerialExecutionStrategy(fetchExceptionHandler))
        .subscriptionExecutionStrategy(new SubscriptionExecutionStrategy(fetchExceptionHandler))
        .build();
  }
}
