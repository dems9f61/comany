package com.takeaway.authorization.oauth.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * User: StMinko Date: 18.11.2019 Time: 12:48
 *
 * <p>
 */
@Slf4j
public class CustomAuthenticationFailureHandler extends OAuth2AccessDeniedHandler
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================

  @Override
  protected ResponseEntity<?> enhanceResponse(ResponseEntity<?> result, Exception authException)
  {
    return ResponseEntity.status(401).body(authException.getLocalizedMessage());
  }

  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
