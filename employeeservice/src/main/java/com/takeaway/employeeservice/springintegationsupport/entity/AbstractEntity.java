package com.takeaway.employeeservice.springintegationsupport.entity;

import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * User: StMinko
 * Date: 14.08.2019
 * Time: 12:08
 * <p/>
 */
@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> implements Persistable<ID>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @NotNull
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(nullable = false)
    private ZonedDateTime modifiedAt;

    @Setter(value = AccessLevel.PRIVATE)
    @Column(nullable = false)
    @Version
    private Long version;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Override
    public final boolean isNew()
    {
        return getId() == null;
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

    @Override
    public String toString()
    {
        return "AbstractEntity{" + "id=" + getId() + ", created at=" + createdAt + ", modified At=" + modifiedAt + '}';
    }

    // =================  protected/package local  Methods ===================

    protected void onPreUpdate()
    {
    }

    protected void onPrePersist()
    {
    }

    protected void onPostLoad()
    {
    }

    // ===========================  private  Methods  ========================

    private void patchAuditData()
    {
        this.modifiedAt = ZonedDateTime.now();
        if (this.createdAt == null)
        {
            this.createdAt = this.modifiedAt;
        }
    }

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
