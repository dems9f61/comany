package com.takeaway.authorization.oauth.control;

import com.google.common.collect.Maps;
import com.takeaway.authorization.oauth.entity.CustomUserDetails;
import com.takeaway.authorization.oauth.entity.CustomUserDetailsEnhancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * User: StMinko Date: 18.11.2019 Time: 12:29
 *
 * <p>
 */
@Slf4j
@Component
public class SecurityInfoTokenEnhancer  implements TokenEnhancer
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  private final CustomUserDetailsEnhancer customUserDetailsEnhancer;

  // ============================  Constructors  ===========================

  @Autowired
  public SecurityInfoTokenEnhancer(CustomUserDetailsEnhancer customUserDetailsEnhancer)
  {
    this.customUserDetailsEnhancer = customUserDetailsEnhancer;
  }

  // ===========================  public  Methods  =========================

  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication)
  {
    LOGGER.info("Calling Token SecurityInfoTokenEnhancer, AccessToken: [{}]. Authentication: [{}]", accessToken, authentication);
    Map<String, Object> additionalInformation = Maps.newHashMap(accessToken.getAdditionalInformation());
    if (accessToken.getAdditionalInformation() == null)
    {
        LOGGER.warn("AccessToken.getAdditionalInformation is null");
    }
    else
    {
        LOGGER.debug("Adding additionalInformation to Access Token");
      if (authentication != null
        && authentication.getUserAuthentication() != null
        && authentication.getUserAuthentication().getPrincipal() != null
        && authentication.getUserAuthentication().getPrincipal() instanceof CustomUserDetails)
      {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getUserAuthentication().getPrincipal();
        additionalInformation.put("user_information", customUserDetailsEnhancer.enhance(userDetails).getUserInformation());
      }
      ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
    }
    return accessToken;
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
