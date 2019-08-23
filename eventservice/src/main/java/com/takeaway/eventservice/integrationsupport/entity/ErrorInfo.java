package com.takeaway.eventservice.integrationsupport.entity;

/*
 * User: StMinko
 * Date: 15.05.2019
 * Time: 14:04
 * <p/>
 */

import lombok.Getter;
import lombok.NonNull;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * Entity to be serialized via REST to the client in case on any error that may occur within any REST Call
 */
@Getter
public final class ErrorInfo
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final String uri;

    private final Instant time;

    private final int httpStatus;

    private final String errorMessage;

    private  final List<ConstraintViolationError> constraintViolations = new LinkedList<>();

    // ============================  Constructors  ===========================

    public ErrorInfo(@NonNull String uri, @NonNull ApiException apiException)
    {
        this.uri = uri;
        this.httpStatus = apiException.getHttpStatus().value();
        this.errorMessage = apiException.getLocalizedMessage();
        this.time = Instant.now();
        Throwable cause = apiException.getCause();
        if (cause != null && ConstraintViolationException.class.isAssignableFrom(cause.getClass()))
        {
            ConstraintViolationException e = (ConstraintViolationException) cause;
            e.getConstraintViolations()
             .forEach(element -> constraintViolations.add(new ConstraintViolationError(element.getRootBeanClass()
                                                                                              .getName(),
                                                                                       element.getPropertyPath()
                                                                                              .toString(),
                                                                                       element.getMessage())));
        }
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================

    @Getter
    public static final class ConstraintViolationError
    {
        private final String rootClass;

        private final String fieldPath;

        private final String errorMessage;

        ConstraintViolationError(@NonNull String rootClass, @NonNull String fieldPath, @NonNull String error)
        {
            this.rootClass = rootClass;
            this.fieldPath = fieldPath;
            this.errorMessage = error;
        }
    }

    // ============================  End of class  ===========================
}
