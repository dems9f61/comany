package com.takeaway.authorization.permission.control;

import com.takeaway.authorization.permission.entity.Permission;
import com.takeaway.authorization.runtime.persistence.JpaAuditedSpecificationRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * User: StMinko Date: 17.10.2019 Time: 11:40
 *
 * <p>
 */
@Repository
public interface PermissionRepository extends JpaAuditedSpecificationRepository<Permission, UUID> {
  // =========================== Class Variables ===========================
  // ==============================  Methods  ==============================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
