package com.takeaway.authorization.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authorization.json.boundary.DataView;
import com.takeaway.authorization.persistence.boundary.AuditedUUIDEntity;
import com.takeaway.authorization.validation.boundary.NullOrNotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

/**
 * User: StMinko Date: 14.10.2019 Time: 10:30
 *
 * <p>
 */
@Audited
@ToString(callSuper = true)
@Setter
@Getter
@Entity
@Table(name = "users",
        schema = "data",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username",
                        columnNames = "userName") },
        indexes = @Index(name = "idx_users_username",
                columnList = "userName"))
@EqualsAndHashCode(callSuper = true)
public class User extends AuditedUUIDEntity
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

    @NotBlank(groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    @NullOrNotBlank(groups = { DataView.PATCH.class })
    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
  private String userName;

  @NotNull(groups = {Default.class, DataView.GET.class})
  @JsonIgnore
  private String passwordHash;

    @NotBlank(groups = { DataView.PUT.class })
    @NullOrNotBlank(groups = { DataView.PATCH.class })
    @JsonView({ DataView.PUT.class, DataView.PATCH.class })
  @Transient
  private String oldPassword;

    @NotBlank(groups = { DataView.PUT.class })
    @NullOrNotBlank(groups = { DataView.PATCH.class })
    @JsonView({ DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
  @Transient
  private String newPassword;

    @NotBlank(groups = { DataView.PUT.class })
    @NullOrNotBlank(groups = { DataView.PATCH.class })
    @JsonView({ DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
  @Transient
  private String confirmPassword;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
