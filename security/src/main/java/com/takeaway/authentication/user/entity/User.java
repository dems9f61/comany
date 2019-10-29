package com.takeaway.authentication.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authentication.integrationsupport.boundary.DataView;
import com.takeaway.authentication.integrationsupport.entity.AuditedUUIDEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

/**
 * User: StMinko Date: 14.10.2019 Time: 10:30
 *
 * <p>
 */
@ToString(callSuper = true)
@Setter
@Getter
@Entity
@Table(name = "users",
    uniqueConstraints = {@UniqueConstraint(name = "uk_users_username",columnNames = "userName")},
    indexes = @Index(name = "idx_users_username",columnList = "userName"))
@EqualsAndHashCode(callSuper = true)
public class User extends AuditedUUIDEntity
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  @NotNull(groups = {Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class})
  @Size(min = 1,
      max = 255,
      groups = {Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class})
  @JsonView({DataView.GET.class, DataView.POST.class, DataView.PUT.class})
  private String userName;

  @NotNull(groups = {Default.class, DataView.GET.class})
  @JsonIgnore
  private String passwordHash;

  @JsonView({DataView.PUT.class})
  @Transient
  private String oldPassword;

  @NotNull(groups = {DataView.POST.class})
  @Size(min = 1,
      max = 255,
      groups = {DataView.POST.class})
  @JsonView({DataView.POST.class, DataView.PUT.class})
  @Transient
  private String newPassword;

  @NotNull(groups = {DataView.POST.class})
  @Size(min = 1,
      max = 255,
      groups = {DataView.POST.class})
  @JsonView({DataView.POST.class, DataView.PUT.class})
  @Transient
  private String confirmPassword;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
