package com.takeaway.authorization.rolepermission.control;

import com.takeaway.authorization.rolepermission.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * User: StMinko Date: 19.10.2019 Time: 16:35
 *
 * <p>
 */
@Repository
interface RolePermissionRepository extends JpaRepository<RolePermission, UUID>
{
  // =========================== Class Variables ===========================
  // ==============================  Methods  ==============================

  @Query("SELECT rp FROM RolePermission rp WHERE rp.role.id = :roleId AND rp.permission.id = :permissionId")
  Optional<RolePermission> findByRoleAndPermission(@Param("roleId") UUID roleId, @Param("permissionId") UUID permissionId);

    // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
