package com.takeaway.eventservice.error;

import lombok.extern.slf4j.Slf4j;
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
        return handleApiException(new InternalServerErrorException(exception.getMessage()));
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