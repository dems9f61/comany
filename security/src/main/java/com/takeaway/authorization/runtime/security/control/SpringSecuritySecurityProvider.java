package com.takeaway.authorization.runtime.security.control;

import com.takeaway.authorization.runtime.security.entity.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Map;
import java.util.UUID;

/**
 * User: StMinko Date: 11.11.2019 Time: 13:55
 *
 * <p>
 */
@Slf4j
public class SpringSecuritySecurityProvider implements EntitySecurityHolder.SecurityProvider
{
    // =========================== Class Variables ===========================

    private static final String DEFAULT_UUID = "e60a3835-fbe3-440c-8003-9b6942426368";

    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Override
    public String getActingUser()
    {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2Authentication)
        {
            OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
            Authentication userAuthentication = auth.getUserAuthentication();
            if (userAuthentication != null)
            {
                String authenticatedUser = userAuthentication.getName();
                if (auth.getDetails() != null)
                {
                    OAuth2AuthenticationDetails authDetails = (OAuth2AuthenticationDetails) auth.getDetails();
                    Object decodedDetails = authDetails.getDecodedDetails();
                    Map<String, Object> userInformationMap = (Map<String, Object>) ((Map<String, Object>) decodedDetails).get("user_information");
                    LOGGER.debug("The authenticated user information [{}] ", authenticatedUser);
                    return userInformationMap.get("userId").toString();
                }
                else if (userAuthentication.getPrincipal() instanceof CustomUserDetails)
                {
                    CustomUserDetails customUserDetails = (CustomUserDetails) userAuthentication.getPrincipal();
                    UUID userId = customUserDetails.getUserInformation().getUserId();
                    LOGGER.debug("The user [{}] with id [{}] is authenticated", authenticatedUser, userId);
                    return userId.toString();
                }
            }
            else
            {
                String clientId = (String) auth.getPrincipal();
                LOGGER.debug("No authenticated user. Client [{}] is therefore the acting user", clientId);
                return clientId;
            }
        }
        LOGGER.debug("No authentication found! Default value [{}] is the acting user", DEFAULT_UUID);
        return DEFAULT_UUID;
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
