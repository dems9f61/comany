package com.takeaway.eventservice.integrationsupport.boundary;

import com.takeaway.eventservice.integrationsupport.entity.ApiException;
import com.takeaway.eventservice.integrationsupport.entity.InternalServerErrorException;
import com.takeaway.eventservice.integrationsupport.entity.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

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
  @ExceptionHandler({ResourceNotFoundException.class})
  public ResponseEntity<String> handleResourceNotFoundException(HttpServletRequest httpServletRequest,
                                                                ResourceNotFoundException resourceNotFoundException)
  {
    return handleApiException(httpServletRequest, resourceNotFoundException);
  }

  @Order(1999)
  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<String> handleException(HttpServletRequest httpServletRequest, Exception exception)
  {
    InternalServerErrorException internalServerErrorException = new InternalServerErrorException(exception);
    return handleApiException(httpServletRequest, internalServerErrorException);
  }

  // ===========================  private  Methods  ========================

    private ResponseEntity<String> handleApiException(HttpServletRequest httpServletRequest, ApiException apiException)
    {
        String localizedMessage = apiException.getLocalizedMessage();
    if (apiException.getHttpStatus().is4xxClientError())
    {
        LOGGER.info("API Error occurred: [{}]", localizedMessage);
    }
    else
    {
        LOGGER.error("Unhandled exception occurred: [{}]", localizedMessage, apiException);
    }
        Instant time = Instant.now();
        String requestURI = httpServletRequest.getRequestURI();

        String sb = "Time: " + time + "\n" + "Requested URI: " + requestURI + "\n" + "Error message: " + localizedMessage;
        return ResponseEntity.status(apiException.getHttpStatus())
                             .body(sb);
  }

  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
