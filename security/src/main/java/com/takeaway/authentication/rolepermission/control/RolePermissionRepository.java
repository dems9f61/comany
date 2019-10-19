package com.takeaway.authentication.rolepermission.control;

import com.takeaway.authentication.permission.entity.Permission;
import com.takeaway.authentication.rolepermission.entity.RolePermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * User: StMinko
 * Date: 19.10.2019
 * Time: 16:35
 * <p/>
 */
@Repository
interface RolePermissionRepository extends JpaRepository<RolePermission, UUID>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    @Query("SELECT rp FROM RolePermission rp WHERE rp.role.id = :roleId AND rp.permission.id = :permissionId")
    Optional<RolePermission> findByRoleAndPermission(@Param("roleId") UUID roleId, @Param("permissionId") UUID permissionId);

    @Query("SELECT rp.permission FROM RolePermission rp WHERE rp.role.id = :roleId")
    Page<Permission> findAllByRole(@Param("roleId") UUID roleId, Pageable pageable);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
