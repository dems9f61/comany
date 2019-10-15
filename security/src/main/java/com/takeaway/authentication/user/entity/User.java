package com.takeaway.authentication.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authentication.integrationsupport.boundary.DataView;
import com.takeaway.authentication.integrationsupport.entity.AuditedUUIDEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

/**
 * User: StMinko
 * Date: 14.10.2019
 * Time: 10:30
 * <p/>
 */
@Setter
@Getter
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username",
                        columnNames = "userName"),
                @UniqueConstraint(name = "uk_users_email",
                        columnNames = "email") },
        indexes = @Index(name = "idx_users_username",
                columnList = "userName"))
@EqualsAndHashCode(callSuper = true)
public class User extends AuditedUUIDEntity
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @NotNull(groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    @Size(min = 1,
            max = 255,
            groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    private String userName;

    @NotNull(groups = { Default.class, DataView.GET.class })
    @JsonIgnore
    private String passwordHash;

    @Email
    @NotNull(groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    private String email;

    @JsonView({ DataView.PUT.class })
    @Transient
    private String oldPassword;

    @NotNull(groups = { DataView.POST.class })
    @Size(min = 1,
            max = 255,
            groups = { DataView.POST.class })
    @JsonView({ DataView.POST.class, DataView.PUT.class })
    @Transient
    private String newPassword;

    @NotNull(groups = { DataView.POST.class })
    @Size(min = 1,
            max = 255,
            groups = { DataView.POST.class })
    @JsonView({ DataView.POST.class, DataView.PUT.class })
    @Transient
    private String confirmPassword;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Override
    public String toString()
    {
        return "User{" + super.toString() + ", userName='" + userName + '\'' + ", passwordHash='" + passwordHash + '\'' + ", email='" + email + '\''
                + ", oldPassword='" + oldPassword + '\'' + ", newPassword='" + newPassword + '\'' + ", confirmPassword='" + confirmPassword + '\''
                + '}';
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
