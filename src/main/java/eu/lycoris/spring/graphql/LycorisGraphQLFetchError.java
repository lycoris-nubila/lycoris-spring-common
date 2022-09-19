package eu.lycoris.spring.graphql;

import eu.lycoris.spring.common.LycorisApplicationException;
import eu.lycoris.spring.common.LycorisAuthenticationException;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.constraints.NotNull;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import static eu.lycoris.spring.common.LycorisMessages.ERROR_WEB_REQUEST_CONSTRAINT_VIOLATION;
import static graphql.Assert.assertNotNull;

@ToString
@EqualsAndHashCode
public class LycorisGraphQLFetchError implements GraphQLError {

  private final @Nullable String message;
  private final transient @NotNull List<Object> path;
  private final @NotNull Throwable exception;
  private final @NotNull List<SourceLocation> locations;
  private final transient @NotNull Map<String, Object> extensions;

  public LycorisGraphQLFetchError(
      @NotNull MessageSource messageSource,
      @NotNull ResultPath path,
      @NotNull Throwable exception,
      @NotNull SourceLocation sourceLocation) {
    this.path = assertNotNull(path).toList();
    this.exception = assertNotNull(exception);
    this.locations = Collections.singletonList(sourceLocation);
    this.extensions = mkExtensions(exception);
    this.message = mkMessage(exception, messageSource);
  }

  @Nullable
  private String mkMessage(@NotNull Throwable exception, @NotNull MessageSource messageSource) {
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

  @NotNull
  private Map<String, Object> mkExtensions(@NotNull Throwable exception) {
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

  public @NotNull Throwable getException() {
    return this.exception;
  }

  @Override
  public @Nullable String getMessage() {
    return this.message;
  }

  @Override
  public @NotNull List<SourceLocation> getLocations() {
    return this.locations;
  }

  @Override
  public @NotNull List<Object> getPath() {
    return this.path;
  }

  @Override
  public @NotNull Map<String, Object> getExtensions() {
    return this.extensions;
  }

  @Override
  public @NotNull ErrorType getErrorType() {
    return ErrorType.DataFetchingException;
  }
}
