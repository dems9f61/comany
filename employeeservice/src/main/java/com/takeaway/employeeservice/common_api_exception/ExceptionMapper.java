package com.takeaway.employeeservice.common_api_exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 17:17
 * <p/>
 */
@Slf4j
@ControllerAdvice
public class ExceptionMapper
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================

    @Order(1)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception)
    {
        List<FieldError> fieldErrors = exception.getBindingResult()
                                                .getFieldErrors();
        List<ObjectError> globalErrors = exception.getBindingResult()
                                                  .getGlobalErrors();
        String fieldErrorMessage = fieldErrors.stream()
                                              .map(fieldError -> String.format("{ Field: %s, Message: %s }",
                                                                               fieldError.getField(),
                                                                               fieldError.getDefaultMessage()))
                                              .collect(joining(","));
        String globalErrorMessage = globalErrors.stream()
                                                .map(objectError -> String.format("{ Object: %s, Message: %s }",
                                                                                  objectError.getObjectName(),
                                                                                  objectError.getDefaultMessage()))
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
    @ExceptionHandler(value = BadRequestException.class)
    protected ResponseEntity<String> handleBadRequestException(BadRequestException badRequestException)
    {
        return handleApiException(badRequestException);
    }

    @Order(3)
    @ExceptionHandler(value = ResourceNotFoundException.class)
    protected ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException)
    {
        return handleApiException(resourceNotFoundException);
    }

    @Order(4)
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException constraintViolationException)
    {
        String errorMessage = "Constraints violated: " + constraintViolationException.getConstraintViolations()
                                                                                     .stream()
                                                                                     .map(violation -> String.format(
                                                                                             "{Property path: :%s, Error message: :%s}",
                                                                                             violation.getPropertyPath(),
                                                                                             violation.getMessage()))
                                                                                     .collect(joining(", ", "[", "]"));
        return handleBadRequestException(new BadRequestException(errorMessage));
    }

    @Order(1999)
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<String> handleException(Exception exception)
    {
        return handleApiException(new InternalServerErrorException(ExceptionUtils.getStackTrace(exception)));
    }

    // ===========================  private  Methods  ========================

    private ResponseEntity<String> handleApiException(ApiException exception)
    {
        LOGGER.error("Unhandled exception occurred", exception);
        return ResponseEntity.status(exception.getHttpStatus())
                             .body(exception.getMessage());
    }

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
