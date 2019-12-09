package com.takeaway.eventservice.runtime.errorhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
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

    @Order(1999)
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<String> handleException(HttpServletRequest httpServletRequest, Exception exception)
    {
        return serializeExceptionToResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR, httpServletRequest);
    }

    // ===========================  private  Methods  ========================

    private ResponseEntity<String> serializeExceptionToResponse(Exception exception, HttpStatus httpStatus, HttpServletRequest httpServletRequest)
    {
        String localizedMessage = exception.getLocalizedMessage();
        if (httpStatus.is4xxClientError())
        {
            LOGGER.info("Client Exception occurred. Error: {}", localizedMessage);
        }
        else
        {
            LOGGER.error("Unhandled Exception occurred. Error: {}", localizedMessage, exception);
        }
        Instant time = Instant.now();
        String requestURI = httpServletRequest.getRequestURI();
        String sb = "Time: " + time + "\n" + "Requested URI: " + requestURI + "\n" + "Error message: " + localizedMessage;
        return ResponseEntity.status(httpStatus.value())
                             .body(exception.getMessage());
    }

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
