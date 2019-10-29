package com.takeaway.eventservice.integrationsupport.boundary;

import com.takeaway.eventservice.integrationsupport.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * User: StMinko Date: 18.03.2019 Time: 17:17
 *
 * <p>
 */
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
  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<ErrorInfo> handleConstraintViolationException(HttpServletRequest httpServletRequest, ConstraintViolationException exception)
  {
    return handleApiException(httpServletRequest, new BadRequestException(exception));
  }

  @Order(2)
  @ExceptionHandler({ResourceNotFoundException.class})
  public ResponseEntity<ErrorInfo> handleResourceNotFoundException(HttpServletRequest httpServletRequest, ResourceNotFoundException resourceNotFoundException)
  {
    return handleApiException(httpServletRequest, resourceNotFoundException);
  }

  @Order(1999)
  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<ErrorInfo> handleException(HttpServletRequest httpServletRequest, Exception exception)
  {
    InternalServerErrorException internalServerErrorException = new InternalServerErrorException(exception);
    return handleApiException(httpServletRequest, internalServerErrorException);
  }

  // ===========================  private  Methods  ========================

  private ResponseEntity<ErrorInfo> handleApiException(HttpServletRequest httpServletRequest, ApiException apiException)
  {
    if (apiException.getHttpStatus().is4xxClientError())
    {
      LOGGER.debug("API Error occurred: [{}]", apiException.getLocalizedMessage(), apiException);
      LOGGER.info("API Error occurred: [{}]", apiException.getLocalizedMessage());
    }
    else
    {
      LOGGER.error("Unhandled exception occurred: [{}]", apiException.getLocalizedMessage(), apiException);
    }
    ErrorInfo errorInfo = new ErrorInfo(httpServletRequest.getRequestURI(), apiException);
    return ResponseEntity.status(apiException.getHttpStatus()).body(errorInfo);
  }

  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
