package com.takeaway.authorization.integrationsupport.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.RevisionType;

import java.io.Serializable;

/**
 * User: StMinko Date: 18.10.2019 Time: 10:26
 *
 * <p>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public final class AuditTrail<ID extends Serializable, T extends AuditedEntity<ID>>
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  private final T entity;

  private final Number revision;

  private final RevisionType revisionType;

  // ============================  Constructors  ===========================

  @JsonCreator
  public AuditTrail(@JsonProperty("entity") T entity, @JsonProperty("revision") Number revision, @JsonProperty("revisionType") RevisionType revisionType)
  {
    this.entity = entity;
    this.revision = revision;
    this.revisionType = revisionType;
  }

  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
