package com.takeaway.authorization.errorhandling.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * User: StMinko Date: 12.09.2019 Time: 10:46
 *
 * <p>
 */
@Getter
@ToString
@RequiredArgsConstructor
public class ServiceException extends Exception
{
  // =========================== Class Variables ===========================

  public enum Reason {
    SUB_RESOURCE_NOT_FOUND,
    RESOURCE_NOT_FOUND,
    INVALID_PARAMETER,
    GENERIC
  }

  // =============================  Variables  =============================

  private final Reason reason;

  public ServiceException(Reason reason, String message)
  {
    super(message);
    this.reason = reason;
  }

  public ServiceException(Reason reason, Exception exception)
  {
    super(exception);
    this.reason = reason;
  }

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
