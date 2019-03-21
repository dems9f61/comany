package com.takeaway.eventservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<String> handleException(Exception exception)
    {
        return handleApiException(new InternalServerErrorException(ExceptionUtils.getStackTrace(exception)));
    }

    private ResponseEntity<String> handleApiException(ApiException exception)
    {
        LOGGER.error("Unhandled exception occurred", exception);
        return ResponseEntity.status(exception.getHttpStatus())
                             .body(exception.getMessage());
    }

    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
