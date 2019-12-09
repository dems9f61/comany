package com.takeaway.authorization.user.control;

import com.takeaway.authorization.permission.entity.Permission;
import com.takeaway.authorization.runtime.persistence.JpaAuditedSpecificationRepository;
import com.takeaway.authorization.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * User: StMinko Date: 21.10.2019 Time: 10:46
 *
 * <p>
 */
@Repository
interface UserRepository extends JpaAuditedSpecificationRepository<User, UUID>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    Optional<User> findByUserName(String userName);

    @Query("SELECT DISTINCT rp.permission FROM User u " + "           JOIN UserRole ur ON ur.user = u "
            + "           JOIN RolePermission rp ON ur.role = rp.role " + "           WHERE u.id = :userId")
    Page<Permission> findAllPermissionsByUser(@Param("userId") UUID userId, Pageable pageable);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
