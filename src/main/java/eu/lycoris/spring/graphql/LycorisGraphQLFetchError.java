package eu.lycoris.spring.graphql;

import static eu.lycoris.spring.common.LycorisMessages.ERROR_WEB_REQUEST_CONSTRAINT_VIOLATION;
import static graphql.Assert.assertNotNull;

import eu.lycoris.spring.common.LycorisApplicationException;
import eu.lycoris.spring.common.LycorisAuthenticationException;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@ToString
@EqualsAndHashCode
@SuppressWarnings("serial")
public class LycorisGraphQLFetchError implements GraphQLError {

  private final String message;
  private final transient List<Object> path;
  private final Throwable exception;
  private final List<SourceLocation> locations;
  private final transient Map<String, Object> extensions;

  public LycorisGraphQLFetchError(
      MessageSource messageSource,
      ResultPath path,
      Throwable exception,
      SourceLocation sourceLocation) {
    this.path = assertNotNull(path).toList();
    this.exception = assertNotNull(exception);
    this.locations = Collections.singletonList(sourceLocation);
    this.extensions = mkExtensions(exception);
    this.message = mkMessage(exception, messageSource);
  }

  private String mkMessage(Throwable exception, MessageSource messageSource) {
    int violationExceptionIndex =
        ExceptionUtils.indexOfThrowable(exception, ConstraintViolationException.class);
    if (violationExceptionIndex >= 0) {
      ConstraintViolationException violationException =
          (ConstraintViolationException)
              ExceptionUtils.getThrowables(exception)[violationExceptionIndex];

      String fields =
          violationException.getConstraintViolations().stream()
              .map(ConstraintViolation::getPropertyPath)
              .map(Path::toString)
              .collect(Collectors.joining(", "));

      return messageSource.getMessage(
          ERROR_WEB_REQUEST_CONSTRAINT_VIOLATION,
          new Object[] {fields},
          ERROR_WEB_REQUEST_CONSTRAINT_VIOLATION,
          LocaleContextHolder.getLocale());
    } else if (exception instanceof UndeclaredThrowableException) {
      UndeclaredThrowableException ute = ((UndeclaredThrowableException) exception);
      exception =
          ute.getUndeclaredThrowable().getCause() instanceof LycorisApplicationException
              ? ute.getUndeclaredThrowable().getCause()
              : ute.getUndeclaredThrowable();

      return messageSource.getMessage(
          exception.getMessage(),
          new Object[] {},
          exception.getMessage(),
          LocaleContextHolder.getLocale());
    } else if (exception instanceof CompletionException) {
      return messageSource.getMessage(
          exception.getCause().getMessage(),
          new Object[] {},
          exception.getCause().getMessage(),
          LocaleContextHolder.getLocale());
    } else {
      return messageSource.getMessage(
          exception.getMessage(),
          new Object[] {},
          exception.getMessage(),
          LocaleContextHolder.getLocale());
    }
  }

  private Map<String, Object> mkExtensions(Throwable exception) {
    Map<String, Object> ext = new HashMap<>();

    if (exception instanceof UndeclaredThrowableException) {
      UndeclaredThrowableException ute = ((UndeclaredThrowableException) exception);
      exception =
          ute.getUndeclaredThrowable().getCause() instanceof LycorisApplicationException
              ? ute.getUndeclaredThrowable().getCause()
              : ute.getUndeclaredThrowable();
    } else if (exception instanceof CompletionException) {
      exception = exception.getCause();
    }

    if (exception instanceof LycorisAuthenticationException) {
      ext.put("type", "AUTHENTICATION");
    } else {
      ext.put("type", "GENERAL");
    }
    return ext;
  }

  public Throwable getException() {
    return exception;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public List<SourceLocation> getLocations() {
    return locations;
  }

  @Override
  public List<Object> getPath() {
    return path;
  }

  @Override
  public Map<String, Object> getExtensions() {
    return extensions;
  }

  @Override
  public ErrorType getErrorType() {
    return ErrorType.DataFetchingException;
  }
}
