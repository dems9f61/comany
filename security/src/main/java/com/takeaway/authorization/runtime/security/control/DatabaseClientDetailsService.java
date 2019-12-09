package com.takeaway.authorization.runtime.security.control;

import com.takeaway.authorization.oauthclient.control.OauthClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

/**
 * User: StMinko Date: 12.11.2019 Time: 11:51
 *
 * <p>
 */
@Profile("!INTEGRATION")
@RequiredArgsConstructor
@Service
public class DatabaseClientDetailsService implements ClientDetailsService
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final OauthClientService oauthClientService;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException
    {
        return oauthClientService.findByClientId(clientId)
                                 .orElseThrow(() -> new NoSuchClientException(String.format("No client with requested id: [%s]", clientId)));
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
