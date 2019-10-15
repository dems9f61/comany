package com.takeaway.authentication.integrationsupport.entity;

import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * User: StMinko
 * Date: 11.10.2019
 * Time: 17:13
 * <p/>
 */
//@Entity
//@Table(name = "audit_trail",
//        schema = "history")
//@RevisionEntity
//@SequenceGenerator(name = "audit_trail_sequence",
//        schema = "history",
//        initialValue = 1,
//        allocationSize = 1)
public class CustomRevisionEntity
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "audit_trail_sequence")
    @RevisionNumber
    private Long revisionNumber;

    @RevisionTimestamp
    private Long revisionTimestamp;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public Long getRevisionNumber()
    {
        return revisionNumber;
    }

    public void setRevisionNumber(Long revisionNumber)
    {
        this.revisionNumber = revisionNumber;
    }

    public Long getRevisionTimestamp()
    {
        return revisionTimestamp;
    }

    public void setRevisionTimestamp(Long revisionTimestamp)
    {
        this.revisionTimestamp = revisionTimestamp;
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
