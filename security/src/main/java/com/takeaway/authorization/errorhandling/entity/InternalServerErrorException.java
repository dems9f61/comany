package com.takeaway.authorization.errorhandling.entity;

/**
 * User: StMinko Date: 18.03.2019 Time: 17:19
 *
 * <p>
 */
public class InternalServerErrorException extends RuntimeException
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================

  public InternalServerErrorException(String message)
  {
    super(message);
  }

  public InternalServerErrorException(String message, Exception exception)
  {
    super(message, exception);
  }

  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
