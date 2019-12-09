package com.takeaway.authorization.permission.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authorization.runtime.persistence.AuditedUUIDEntity;
import com.takeaway.authorization.runtime.rest.DataView;
import com.takeaway.authorization.runtime.rest.NullOrNotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

/**
 * User: StMinko Date: 14.10.2019 Time: 12:28
 *
 * <p>
 */
@Audited
@Setter
@Getter
@Entity
@Table(name = "permissions",
        schema = "data",
        uniqueConstraints = @UniqueConstraint(name = "uk_permissions_name",
                columnNames = "name"),
        indexes = @Index(name = "idx_permissions_name",
                columnList = "name"))
@EqualsAndHashCode(callSuper = true)
@ToString
public class Permission extends AuditedUUIDEntity
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @NotBlank(groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    @NullOrNotBlank(groups = { DataView.PATCH.class })
    @JsonView({ DataView.GET.class, DataView.PUT.class, DataView.PATCH.class, DataView.POST.class })
    @Size(min = 1,
            max = 255,
            groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    private String name;

    @NotBlank(groups = { DataView.PUT.class })
    @NullOrNotBlank(groups = { DataView.PATCH.class, DataView.POST.class })
    @JsonView({ DataView.GET.class, DataView.PUT.class, DataView.PATCH.class, DataView.POST.class })
    private String description;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
