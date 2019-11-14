package com.takeaway.authorization.oauth.control;

import com.takeaway.authorization.auditing.boundary.AbstractDefaultAuditedEntityService;
import com.takeaway.authorization.oauth.entity.OauthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.Optional;
import java.util.UUID;

/**
 * User: StMinko Date: 06.11.2019 Time: 11:17
 * <p>
 */
@Service
public class OauthClientService extends AbstractDefaultAuditedEntityService<OauthClientRepository, OauthClient, UUID>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================

    @Autowired
    public OauthClientService(OauthClientRepository repository, Validator validator)
    {
        super(repository, validator);
    }

    // ===========================  public  Methods  =========================

    public Optional<OauthClient> findByClientId(String clientId)
    {
        return getRepository().findByClientId(clientId);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
