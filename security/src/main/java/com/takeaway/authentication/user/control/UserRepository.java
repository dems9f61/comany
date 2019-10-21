package com.takeaway.authentication.user.control;

import com.takeaway.authentication.integrationsupport.control.JpaAuditedSpecificationRepository;
import com.takeaway.authentication.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * User: StMinko
 * Date: 21.10.2019
 * Time: 10:46
 * <p/>
 */
@Repository
interface UserRepository extends JpaAuditedSpecificationRepository<User, UUID>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    Optional<User> findByUserName(String userName);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
