package com.takeaway.authentication.userrole.control;

import com.takeaway.authentication.userrole.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * User: StMinko Date: 30.10.2019 Time: 11:39
 * <p>
 */
@Repository
interface UserRoleRepository extends JpaRepository<UserRole, UUID>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.id = :permissionId")
    Optional<UserRole> findByUserAndPermission(@Param("userId") UUID userId, @Param("permissionId") UUID permissionId);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
