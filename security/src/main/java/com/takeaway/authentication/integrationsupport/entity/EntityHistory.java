package com.takeaway.authentication.integrationsupport.entity;

import org.hibernate.envers.RevisionType;

/**
 * User: StMinko
 * Date: 17.10.2019
 * Time: 16:06
 * <p/>
 */
public interface EntityHistory<ENTITY>
{
    ENTITY getEntity();

    Number getRevision();

    RevisionType getRevisionType();
}

