package com.takeaway.authorization.errorhandling.boundary;

import com.takeaway.authorization.errorhandling.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static java.util.stream.Collectors.joining;

@Slf4j
@ControllerAdvice
public class GlobalExceptionMapper
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================

  @Order(1)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception)
  {
    List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
    List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
    String fieldErrorMessage = fieldErrors.stream()
              .map(fieldError -> String.format("{ Field: %s, Message: %s }", fieldError.getField(), fieldError.getDefaultMessage()))
              .collect(joining(","));
    String globalErrorMessage = globalErrors.stream()
              .map(objectError -> String.format("{ Object: %s, Message: %s }", objectError.getObjectName(), objectError.getDefaultMessage()))
              .collect(joining(","));

    String errorMsg = "Constraint(s) failed: ";
    if (StringUtils.trimToNull(fieldErrorMessage) != null)
    {
      errorMsg += fieldErrorMessage;
    }
    if (StringUtils.trimToNull(globalErrorMessage) != null)
    {
      errorMsg += globalErrorMessage;
    }
    return handleApiException(new BadRequestException(errorMsg));
  }

  @Order(2)
  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException constraintViolationException)
  {
    String errorMessage = "Constraints violated: "
          + constraintViolationException.getConstraintViolations().stream()
                  .map(violation -> String.format("{Property path: :%s, Error message: :%s}", violation.getPropertyPath(), violation.getMessage()))
                  .collect(joining(", ", "[", "]"));
    return handleBadRequestException(new BadRequestException(errorMessage));
  }

  @Order(3)
  @ExceptionHandler(value = HttpMessageNotReadableException.class)
  ResponseEntity<String> handleNotReadableException(HttpMessageNotReadableException notReadableException)
  {
    Throwable mostSpecificCause = notReadableException.getMostSpecificCause();
    String errorMessage = mostSpecificCause != null ? mostSpecificCause.getMessage() : notReadableException.getMessage();
    return handleBadRequestException(new BadRequestException(errorMessage));
  }

  @Order(4)
  @ExceptionHandler(value = BadRequestException.class)
  ResponseEntity<String> handleBadRequestException(BadRequestException badRequestException)
  {
    return handleApiException(badRequestException);
  }

  @Order(5)
  @ExceptionHandler(value = ResourceNotFoundException.class)
  ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException)
  {
    return handleApiException(resourceNotFoundException);
  }

  @Order(6)
  @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
  ResponseEntity<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException mediaNotSupportedException)
  {
    String errorMsg = "Unsupported content type: " + mediaNotSupportedException.getContentType();
    errorMsg += "\nSupported content types: " + MediaType.toString(mediaNotSupportedException.getSupportedMediaTypes());
    return handleApiException(new BadRequestException(errorMsg));
  }

  @Order(7)
  @ExceptionHandler(value = TransactionSystemException.class)
  ResponseEntity<String> handleTransactionSystemException(TransactionSystemException exception)
  {
    return handleNestedRuntimeException(exception);
  }

  @Order(8)
  @ExceptionHandler(value = InvalidDataAccessApiUsageException.class)
  ResponseEntity<String> handleTransactionSystemException(InvalidDataAccessApiUsageException exception)
  {
    return handleNestedRuntimeException(exception);
  }

  @Order(9)
  @ExceptionHandler(value = DataIntegrityViolationException.class)
  ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException exception)
  {
    return handleNestedRuntimeException(exception);
  }

    @Order(10)
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<String> handleDAccessDeniedException(AccessDeniedException exception)
    {
        return handleApiException(new NotAuthorizedException(exception.getMessage()));
    }

  @Order(1999)
  @ExceptionHandler(value = {Exception.class})
  ResponseEntity<String> handleException(Exception exception)
  {
      return handleApiException(new InternalServerErrorException(exception.getMessage(), exception));
  }

  // ===========================  private  Methods  ========================

  private ResponseEntity<String> handleNestedRuntimeException(NestedRuntimeException exception)
  {
    Throwable mostSpecificCause = exception.getMostSpecificCause();
    if (mostSpecificCause instanceof ConstraintViolationException)
    {
      return handleConstraintViolationException((ConstraintViolationException) mostSpecificCause);
    }
    if (mostSpecificCause instanceof IllegalArgumentException)
    {
      IllegalArgumentException illegalArgumentException = (IllegalArgumentException) mostSpecificCause;
      return handleBadRequestException(new BadRequestException(illegalArgumentException));
    }
    return handleException(exception);
  }

  private ResponseEntity<String> handleApiException(ApiException apiException)
  {
    HttpStatus httpStatus = apiException.getHttpStatus();
    if (httpStatus.is4xxClientError())
    {
        LOGGER.info("Client error occurred: [{}]", apiException.getLocalizedMessage());
    }
    else
    {
      LOGGER.error("Unhandled exception occurred: [{}]", apiException.getLocalizedMessage(), apiException);
    }
    return ResponseEntity.status(httpStatus).body(apiException.getMessage());
  }

  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
