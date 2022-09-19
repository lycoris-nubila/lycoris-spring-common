package eu.lycoris.spring.graphql;

import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

import javax.validation.constraints.NotNull;

@Slf4j
public class LycorisGraphQLFetchExceptionHandler implements DataFetcherExceptionHandler {

  private final @NotNull MessageSource messageSource;

  public LycorisGraphQLFetchExceptionHandler(@NotNull MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public @NotNull DataFetcherExceptionHandlerResult onException(
      DataFetcherExceptionHandlerParameters handlerParameters) {
    LycorisGraphQLFetchError error =
        new LycorisGraphQLFetchError(
            this.messageSource,
            handlerParameters.getPath(),
            handlerParameters.getException(),
            handlerParameters.getSourceLocation());

    log.info(error.getMessage(), handlerParameters.getException());

    return DataFetcherExceptionHandlerResult.newResult().error(error).build();
  }
}
