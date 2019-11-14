package com.takeaway.eventservice.errorhandling.entity;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * User: StMinko Date: 18.03.2019 Time: 11:17
 *
 * <p>
 */
public class ApiException extends RuntimeException
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  @Getter
  private final HttpStatus httpStatus;

  // ============================  Constructors  ===========================

  public ApiException(HttpStatus httpStatus, String message)
  {
    super(message);
    this.httpStatus = httpStatus;
  }

  public ApiException(HttpStatus httpStatus, Throwable cause)
  {
    super(cause);
    this.httpStatus = httpStatus;
  }

  public ApiException(HttpStatus httpStatus, Throwable cause, String message)
  {
    super(message, cause);
    this.httpStatus = httpStatus;
  }

  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
