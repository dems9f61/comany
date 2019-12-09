package com.takeaway.authorization.oauthclient.control;

import com.takeaway.authorization.AbstractRepositoryTestHelper;
import com.takeaway.authorization.oauthclient.entity.OauthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * User: StMinko Date: 06.11.2019 Time: 12:37
 *
 * <p>
 */
@Component
@Transactional
public class OAuthClientRepositoryTestHelper extends AbstractRepositoryTestHelper<OauthClient, UUID, OauthClientRepository>
{
    @Autowired
    public OAuthClientRepositoryTestHelper(OauthClientRepository repository)
    {
        super(repository);
    }
}
