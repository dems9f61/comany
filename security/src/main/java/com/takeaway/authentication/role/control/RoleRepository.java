package com.takeaway.authentication.role.control;

import com.takeaway.authentication.integrationsupport.control.JpaSpecificationRepository;
import com.takeaway.authentication.role.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * User: StMinko
 * Date: 14.10.2019
 * Time: 13:44
 * <p/>
 */
@Repository
interface RoleRepository extends JpaSpecificationRepository<Role, UUID>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
