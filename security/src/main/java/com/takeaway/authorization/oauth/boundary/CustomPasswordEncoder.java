package com.takeaway.authorization.oauth.boundary;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * User: StMinko Date: 21.10.2019 Time: 11:34
 *
 * <p>
 */
@Slf4j
@Component
public class CustomPasswordEncoder implements PasswordEncoder
{
  // =========================== Class Variables ===========================

  private static final String SALT = "21377353-84af-4c3e-8979-fdc4c582a9f0";

  private static final int ITERATIONS = 42;

  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  @Override
  public String encode(CharSequence rawPassword)
  {
    try
    {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] toDigest = (SALT + rawPassword).getBytes(StandardCharsets.UTF_8);
      for (int i = 0; i < ITERATIONS; i++)
      {
        toDigest = digest.digest(toDigest);
      }
      return Hex.encodeHexString(toDigest);
    }
    catch (NoSuchAlgorithmException e)
    {
      LOGGER.warn("Encoding Password failed: [{}]", e.getMessage(), e);
    }
    return null;
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword)
  {
    return encodedPassword != null && encodedPassword.equals(encode(rawPassword));
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
