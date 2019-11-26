package com.takeaway.authorization.runtime.security.boundary;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * User: StMinko Date: 15.11.2019 Time: 16:04
 *
 * <p>
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, // This enables Spring Security pre/post annotations
      securedEnabled = true, // This enables the @Secured annotation
      jsr250Enabled = true) // Allows us to use the @RoleAllowed annotation
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
