package com.takeaway.authentication.rolepermission.entity;

import com.takeaway.authentication.integrationsupport.entity.UUIDEntity;
import com.takeaway.authentication.permission.entity.Permission;
import com.takeaway.authentication.role.entity.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * User: StMinko
 * Date: 19.10.2019
 * Time: 16:00
 * <p/>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "role_permissions",
        uniqueConstraints = @UniqueConstraint(name = "uk_role_permissions_role_id_permission_id",
                columnNames = { "role_id", "permission_id" }))
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
