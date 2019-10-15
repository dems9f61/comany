package com.takeaway.authentication.integrationsupport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authentication.integrationsupport.boundary.DataView;
import com.takeaway.authentication.integrationsupport.control.EntitySecurityHolder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.NotAudited;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * User: StMinko
 * Date: 11.10.2019
 * Time: 16:11
 * <p/>
 */

@Getter
@Setter
@EqualsAndHashCode(exclude = { "createdAt", "createdBy", "lastUpdatedAt", "lastUpdatedBy" })
@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> implements Persistable<ID>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Id
    @JsonView(DataView.GET.class)
    private ID id;

    @NotNull(groups = { Default.class, DataView.GET.class })
    @JsonView(DataView.GET.class)
    @NotAudited
    private ZonedDateTime createdAt;

    @NotNull(groups = { Default.class, DataView.GET.class })
    @JsonView(DataView.GET.class)
    @NotAudited
    private UUID createdBy;

    @NotNull(groups = { Default.class, DataView.GET.class })
    @JsonView(DataView.GET.class)
    private ZonedDateTime lastUpdatedAt;

    @NotNull(groups = { Default.class, DataView.GET.class })
    @JsonView(DataView.GET.class)
    private UUID lastUpdatedBy;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Override
    public final ID getId()
    {
        return id;
    }

    public final void setId(ID id)
    {
        this.id = id;
    }

    private void patchAuditData()
    {
        this.lastUpdatedAt = ZonedDateTime.now();
        this.lastUpdatedBy = EntitySecurityHolder.get()
                                                 .getUser();
        if (this.createdAt == null)
        {
            this.createdAt = this.lastUpdatedAt;
            this.createdBy = this.lastUpdatedBy;
        }
    }

    @PrePersist
    public final void prePersist()
    {
        patchAuditData();
        onPrePersist();
    }

    @PreUpdate
    public final void preUpdate()
    {
        patchAuditData();
        onPreUpdate();
    }

    @PostLoad
    public final void postLoad()
    {
        onPostLoad();
    }

    protected void onPreUpdate()
    {
    }

    protected void onPrePersist()
    {
    }

    protected void onPostLoad()
    {
    }

    @Override
    @JsonIgnore
    public final boolean isNew()
    {
        return id == null;
    }

    @Override
    public String toString()
    {
        return "AbstractEntity{" + "id=" + id + ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", lastUpdatedAt=" + lastUpdatedAt
                + ", lastUpdatedBy=" + lastUpdatedBy + '}';
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
