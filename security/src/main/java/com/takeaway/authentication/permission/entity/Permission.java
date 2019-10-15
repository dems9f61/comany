package com.takeaway.authentication.permission.entity;

import com.takeaway.authentication.integrationsupport.boundary.DataView;
import com.takeaway.authentication.integrationsupport.entity.AuditedUUIDEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

/**
 * User: StMinko
 * Date: 14.10.2019
 * Time: 12:28
 * <p/>
 */
@Setter
@Getter
@Entity
@Table(name = "permissions",
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

    @NotNull(groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    @Size(min = 1,
            max = 255,
            groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    private String name;

    @NotNull(groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    @Size(min = 1,
            max = 8192,
            groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    private String description;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
