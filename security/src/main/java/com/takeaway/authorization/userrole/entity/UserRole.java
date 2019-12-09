package com.takeaway.authorization.userrole.entity;

import com.takeaway.authorization.role.entity.Role;
import com.takeaway.authorization.runtime.persistence.UUIDEntity;
import com.takeaway.authorization.user.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * User: StMinko Date: 30.10.2019 Time: 11:35
 *
 * <p>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "user_roles",
            schema = "data",
            uniqueConstraints =
                @UniqueConstraint(name = "uk_user_roles_user_id_permission_id",
                        columnNames = {"user_id", "role_id"}))
public class UserRole extends UUIDEntity
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id")
    private Role role;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
