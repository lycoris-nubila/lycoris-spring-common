package eu.lycoris.spring.web;

import static eu.lycoris.spring.common.LycorisMessages.ERROR_WEB_REQUEST_INVALID_FIELD;
import static eu.lycoris.spring.common.LycorisMessages.ERROR_WEB_REQUEST_INVALID_INPUT;

import eu.lycoris.spring.common.LycorisErrorDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

@ControllerAdvice
@ConditionalOnBean({MessageSource.class})
public class LycorisControllerAdvice extends ResponseEntityExceptionHandler {

  @Autowired private MessageSource messageSource;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return handleObjectErrorList(
        ex, ex.getBindingResult().getAllErrors(), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(
      BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return handleObjectErrorList(ex, ex.getAllErrors(), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    Map<String, String> fieldErrorMessage = new HashMap<>();
    fieldErrorMessage.put(
        ex.getPropertyName(),
        messageSource.getMessage(
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

  private ResponseEntity<Object> handleObjectErrorList(
      Exception ex,
      List<ObjectError> errors,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    Map<String, String> fieldErrorMessages =
        errors.stream()
            .filter(error -> error instanceof FieldError)
            .map(error -> (FieldError) error)
            .collect(
                Collectors.toMap(
                    FieldError::getField,
                    fieldError ->
                        messageSource.getMessage(
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
