package eu.lycoris.spring.graphql;

import org.springframework.context.MessageSource;

import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.ExecutionPath;
import graphql.language.SourceLocation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LycorisGraphQLFetchExceptionHandler implements DataFetcherExceptionHandler {

  private final MessageSource messageSource;

  public LycorisGraphQLFetchExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public void accept(DataFetcherExceptionHandlerParameters handlerParameters) {
    Throwable exception = handlerParameters.getException();
    SourceLocation sourceLocation = handlerParameters.getField().getSourceLocation();
    ExecutionPath path = handlerParameters.getPath();

    LycorisGraphQLFetchError error =
        new LycorisGraphQLFetchError(messageSource, path, exception, sourceLocation);
    handlerParameters.getExecutionContext().addError(error);
    log.warn(error.getMessage(), exception);
  }
}
