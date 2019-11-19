package com.takeaway.authorization.oauth.entity;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;

/**
 * User: StMinko Date: 18.11.2019 Time: 10:17
 *
 * <p>
 */
@Component
public class CustomJwtTokenStore extends JwtTokenStore
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================

  public CustomJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer)
  {
    super(jwtTokenEnhancer);
  }

  // ===========================  public  Methods  =========================

  @Override
  public OAuth2Authentication readAuthentication(OAuth2AccessToken token)
  {
    OAuth2Authentication oAuth2Authentication = super.readAuthentication(token);
    oAuth2Authentication.setDetails(token.getAdditionalInformation());
    return oAuth2Authentication;
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
