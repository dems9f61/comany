package com.takeaway.authorization.role.control;

import com.takeaway.authorization.persistence.boundary.JpaAuditedSpecificationRepository;
import com.takeaway.authorization.role.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * User: StMinko Date: 14.10.2019 Time: 13:44
 *
 * <p>
 */
@Repository
interface RoleRepository extends JpaAuditedSpecificationRepository<Role, UUID> {
  // =========================== Class Variables ===========================
  // ==============================  Methods  ==============================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
