package com.takeaway.eventservice.integrationsupport.boundary;

import com.takeaway.eventservice.integrationsupport.entity.ApiException;
import com.takeaway.eventservice.integrationsupport.entity.BadRequestException;
import com.takeaway.eventservice.integrationsupport.entity.ErrorInfo;
import com.takeaway.eventservice.integrationsupport.entity.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 17:17
 * <p/>
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
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<ErrorInfo> handleConstraintViolationException(HttpServletRequest httpServletRequest, ConstraintViolationException exception)
    {
        return handleApiException(httpServletRequest, new BadRequestException(exception));
    }


    @Order(1999)
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<ErrorInfo> handleException(HttpServletRequest httpServletRequest, Exception exception)
    {
        InternalServerErrorException internalServerErrorException = new InternalServerErrorException(exception);
        return handleApiException(httpServletRequest, internalServerErrorException);
    }

    // ===========================  private  Methods  ========================

    private ResponseEntity<ErrorInfo> handleApiException(HttpServletRequest httpServletRequest, ApiException exception)
    {
        LOGGER.error("Unhandled exception occurred", exception);
        ErrorInfo errorInfo = new ErrorInfo(httpServletRequest.getRequestURI(), exception);
        return ResponseEntity.status(exception.getHttpStatus())
                             .body(errorInfo);
    }


    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
