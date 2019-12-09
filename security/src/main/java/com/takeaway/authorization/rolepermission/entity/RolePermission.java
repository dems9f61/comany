package com.takeaway.authorization.rolepermission.entity;

import com.takeaway.authorization.permission.entity.Permission;
import com.takeaway.authorization.role.entity.Role;
import com.takeaway.authorization.runtime.persistence.UUIDEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * User: StMinko Date: 19.10.2019 Time: 16:00
 *
 * <p>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "role_permissions",
            schema = "data",
            uniqueConstraints =
                @UniqueConstraint(name = "uk_role_permissions_role_id_permission_id",
                        columnNames = {"role_id", "permission_id"}))
public class RolePermission extends UUIDEntity
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id")
    private Role role;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
