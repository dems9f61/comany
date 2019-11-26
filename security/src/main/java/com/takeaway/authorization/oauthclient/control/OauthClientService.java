package com.takeaway.authorization.oauthclient.control;

import com.takeaway.authorization.oauthclient.entity.OauthClient;
import com.takeaway.authorization.runtime.auditing.boundary.AbstractDefaultAuditedEntityService;
import com.takeaway.authorization.runtime.errorhandling.entity.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Validator;
import java.util.Optional;
import java.util.UUID;

/**
 * User: StMinko Date: 06.11.2019 Time: 11:17
 * <p>
 */
@Transactional
@Validated
@Service
public class OauthClientService extends AbstractDefaultAuditedEntityService<OauthClientRepository, OauthClient, UUID>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final PasswordEncoder passwordEncoder;

    // ============================  Constructors  ===========================

    @Autowired
    public OauthClientService(OauthClientRepository repository, Validator validator, PasswordEncoder passwordEncoder)
    {
        super(repository, validator);
        this.passwordEncoder = passwordEncoder;
    }

    // ===========================  public  Methods  =========================

    @Transactional(propagation = Propagation.SUPPORTS)
    public Optional<OauthClient> findByClientId(String clientId)
    {
        return getRepository().findByClientId(clientId);
    }

    // =================  protected/package local  Methods ===================

    @Override
    protected OauthClient onBeforeCreate(OauthClient create)
    {
        super.onBeforeCreate(create);
        String clientId = create.getClientId();
        if (clientId != null && findByClientId(clientId).isPresent())
        {
            throw new BadRequestException("The specified client id exists already!");
        }
        if (create.getNewClientSecret() == null || create.getConfirmClientSecret() == null)
        {
            throw new BadRequestException("Password creation requires a new secret and a confirm secret!");
        }
        if (!create.getNewClientSecret()
                   .equals(create.getConfirmClientSecret()))
        {
            throw new BadRequestException("New Password and Confirm Password do not match!");
        }
        create.setClientSecretHash(passwordEncoder.encode(create.getNewClientSecret()));
        return create;
    }

    @Override
    protected OauthClient onBeforeUpdate(OauthClient existing, OauthClient update)
    {
        super.onBeforeUpdate(existing, update);
        String clientId = update.getClientId();
        if (clientId != null && findByClientId(clientId).isPresent())
        {
            throw new BadRequestException("The specified client id exists already!");
        }
        if (update.getOldClientSecret() != null)
        {
            if (!passwordEncoder.encode(update.getOldClientSecret())
                                .equals(existing.getClientSecretHash()))
            {
                throw new BadRequestException("Password update - Bad Credentials");
            }
            if (update.getNewClientSecret() == null || update.getConfirmClientSecret() == null)
            {
                throw new BadRequestException("Password update - Bad Credentials");
            }
            if (!update.getNewClientSecret()
                       .equals(update.getConfirmClientSecret()))
            {
                throw new BadRequestException("Password update - Bad Credentials");
            }
            update.setClientSecretHash(passwordEncoder.encode(update.getNewClientSecret()));
        }

        return update;
    }

    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
