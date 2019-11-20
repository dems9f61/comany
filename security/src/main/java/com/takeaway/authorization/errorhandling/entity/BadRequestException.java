package com.takeaway.authorization.errorhandling.entity;

/**
 * User: StMinko Date: 18.03.2019 Time: 11:22
 *
 * <p>
 */
public class BadRequestException extends RuntimeException
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================

  public BadRequestException(Throwable cause)
  {
    super(cause);
  }

  public BadRequestException(String message)
  {
    super(message);
  }

  public BadRequestException(String message, Throwable cause)
  {
    super(message, cause);
  }

  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
