package com.takeaway.authorization.persistence.boundary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authorization.json.boundary.DataView;
import com.takeaway.authorization.oauth.control.EntitySecurityHolder;
import lombok.AccessLevel;
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

/**
 * User: StMinko Date: 11.10.2019 Time: 16:11
 *
 * <p>
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = {"createdAt", "createdBy", "lastUpdatedAt", "lastUpdatedBy", "version"})
@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> implements Persistable<ID>
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  @Id
  @JsonView(DataView.GET.class)
  private ID id;

  @NotNull(groups = {Default.class, DataView.GET.class})
  @JsonView(DataView.GET.class)
  @NotAudited
  private ZonedDateTime createdAt;

  @NotNull(groups = {Default.class, DataView.GET.class})
  @JsonView(DataView.GET.class)
  @NotAudited
  private String createdBy;

  @NotNull(groups = {Default.class, DataView.GET.class})
  @JsonView(DataView.GET.class)
  private ZonedDateTime lastUpdatedAt;

  @NotNull(groups = {Default.class, DataView.GET.class})
  @JsonView(DataView.GET.class)
  private String lastUpdatedBy;

  @JsonView({DataView.GET.class, DataView.PUT.class, DataView.PATCH.class})
  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false)
  @Version
  private Long version;

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
                                               .getActingUser();
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

  protected void onPreUpdate() {}

  protected void onPrePersist() {}

  protected void onPostLoad() {}

  @Override
  @JsonIgnore
  public final boolean isNew()
  {
    return id == null;
  }

  @Override
  public String toString()
  {
    return "AbstractEntity{"
      + "id="
      + id
      + ", createdAt="
      + createdAt
      + ", createdBy="
      + createdBy
      + ", lastUpdatedAt="
      + lastUpdatedAt
      + ", lastUpdatedBy="
      + lastUpdatedBy
      + '}';
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
