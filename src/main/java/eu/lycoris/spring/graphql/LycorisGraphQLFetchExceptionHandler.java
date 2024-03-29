package eu.lycoris.spring.graphql;

import org.springframework.context.MessageSource;

import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LycorisGraphQLFetchExceptionHandler implements DataFetcherExceptionHandler {

  private final MessageSource messageSource;

  public LycorisGraphQLFetchExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public DataFetcherExceptionHandlerResult onException(
      DataFetcherExceptionHandlerParameters handlerParameters) {
    LycorisGraphQLFetchError error =
        new LycorisGraphQLFetchError(
            messageSource,
            handlerParameters.getPath(),
            handlerParameters.getException(),
            handlerParameters.getSourceLocation());

    log.info(error.getMessage(), handlerParameters.getException());

    return DataFetcherExceptionHandlerResult.newResult().error(error).build();
  }
}
