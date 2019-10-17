package com.takeaway.authentication.permission.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.takeaway.authentication.integrationsupport.entity.EntityHistory;
import lombok.Getter;
import org.hibernate.envers.RevisionType;

/**
 * User: StMinko
 * Date: 17.10.2019
 * Time: 16:24
 * <p/>
 */
@Getter
public class PermissionHistory implements EntityHistory<Permission>
{
    private final Permission entity;

    private final Number revision;

    private final RevisionType revisionType;

    @JsonCreator
    public PermissionHistory(@JsonProperty("entity") Permission entity,
                             @JsonProperty("revision") Number revision,
                             @JsonProperty("revisionType") RevisionType revisionType)
    {
        this.entity = entity;
        this.revision = revision;
        this.revisionType = revisionType;
    }
}
