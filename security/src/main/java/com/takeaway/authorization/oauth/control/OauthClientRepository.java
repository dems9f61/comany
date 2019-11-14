package com.takeaway.authorization.oauth.control;

import com.takeaway.authorization.oauth.entity.OauthClient;
import com.takeaway.authorization.persistence.boundary.JpaAuditedSpecificationRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * User: StMinko Date: 06.11.2019 Time: 11:13
 *
 * <p>
 */
@Repository
interface OauthClientRepository extends JpaAuditedSpecificationRepository<OauthClient, UUID>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    Optional<OauthClient> findByClientId(String clientId);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
