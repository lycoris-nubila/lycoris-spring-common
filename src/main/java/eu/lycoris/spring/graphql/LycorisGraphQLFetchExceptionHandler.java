package eu.lycoris.spring.graphql;

import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

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

    log.error(error.getMessage(), handlerParameters.getException());

    return DataFetcherExceptionHandlerResult.newResult().error(error).build();
  }
}
