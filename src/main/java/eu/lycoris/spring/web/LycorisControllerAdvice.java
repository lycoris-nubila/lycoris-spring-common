package eu.lycoris.spring.web;

import eu.lycoris.spring.common.LycorisErrorDto;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static eu.lycoris.spring.common.LycorisMessages.ERROR_WEB_REQUEST_INVALID_FIELD;
import static eu.lycoris.spring.common.LycorisMessages.ERROR_WEB_REQUEST_INVALID_INPUT;

@ControllerAdvice
@ConditionalOnBean({MessageSource.class})
public class LycorisControllerAdvice extends ResponseEntityExceptionHandler {

  private final @NotNull MessageSource messageSource;

  public LycorisControllerAdvice(@NotNull MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }

  @Override
  protected @NotNull ResponseEntity<Object> handleMethodArgumentNotValid(
      @NotNull MethodArgumentNotValidException ex,
      @NotNull HttpHeaders headers,
      @NotNull HttpStatus status,
      @NotNull WebRequest request) {
    return handleObjectErrorList(
        ex, ex.getBindingResult().getAllErrors(), headers, status, request);
  }

  @Override
  protected @NotNull ResponseEntity<Object> handleBindException(
      @NotNull BindException ex,
      @NotNull HttpHeaders headers,
      @NotNull HttpStatus status,
      @NotNull WebRequest request) {
    return handleObjectErrorList(ex, ex.getAllErrors(), headers, status, request);
  }

  @Override
  protected @NotNull ResponseEntity<Object> handleTypeMismatch(
      @NotNull TypeMismatchException ex,
      @NotNull HttpHeaders headers,
      @NotNull HttpStatus status,
      @NotNull WebRequest request) {

    Map<String, String> fieldErrorMessage = new HashMap<>();
    fieldErrorMessage.put(
        ex.getPropertyName(),
        this.messageSource.getMessage(
            ERROR_WEB_REQUEST_INVALID_FIELD,
            new Object[] {ex.getPropertyName()},
            ex.getMessage(),
            LocaleContextHolder.getLocale()));

    return super.handleExceptionInternal(
        ex,
        LycorisErrorDto.builder(ERROR_WEB_REQUEST_INVALID_INPUT)
            .fieldErrorList(fieldErrorMessage)
            .build(),
        headers,
        status,
        request);
  }

  private @NotNull ResponseEntity<Object> handleObjectErrorList(
      @NotNull Exception ex,
      @NotNull List<ObjectError> errors,
      @NotNull HttpHeaders headers,
      @NotNull HttpStatus status,
      @NotNull WebRequest request) {

    Map<String, String> fieldErrorMessages =
        errors.stream()
            .filter(error -> error instanceof FieldError)
            .map(error -> (FieldError) error)
            .collect(
                Collectors.toMap(
                    FieldError::getField,
                    fieldError ->
                        this.messageSource.getMessage(
                            ERROR_WEB_REQUEST_INVALID_FIELD,
                            new Object[] {fieldError.getField()},
                            fieldError.getDefaultMessage(),
                            LocaleContextHolder.getLocale())));

    List<String> errorMessages =
        errors.stream()
            .filter(error -> !(error instanceof FieldError))
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.toList());

    return super.handleExceptionInternal(
        ex,
        LycorisErrorDto.builder(ERROR_WEB_REQUEST_INVALID_INPUT)
            .errorList(errorMessages)
            .fieldErrorList(fieldErrorMessages)
            .build(),
        headers,
        status,
        request);
  }
}
