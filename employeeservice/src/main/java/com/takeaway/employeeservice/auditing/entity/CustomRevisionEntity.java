package com.takeaway.employeeservice.auditing.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;

@Entity
@Table(name = "audit_trail",
        schema = "history")
@RevisionEntity
@SequenceGenerator(name = "audit_trail_sequence",
        allocationSize = 1,
        sequenceName = "audit_trail_sequence",
        schema = "history")
public class CustomRevisionEntity
{
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "audit_trail_sequence")
    @RevisionNumber
    private Long revisionNumber;

    @Setter
    @Getter
    @RevisionTimestamp
    private Long revisionTimestamp;
}
