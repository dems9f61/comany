package com.takeaway.authentication.permission.control;

import com.takeaway.authentication.integrationsupport.control.JpaAuditedSpecificationRepository;
import com.takeaway.authentication.permission.entity.Permission;
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
