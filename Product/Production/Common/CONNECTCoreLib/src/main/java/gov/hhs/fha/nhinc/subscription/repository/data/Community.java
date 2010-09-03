/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription.repository.data;

import java.io.Serializable;

/**
 * Data class for a community
 * 
 * @author Neil Webb
 */
public class Community implements Serializable
{
    private static final long serialVersionUID = -6937227984548195062L;
    private String communityId;
    private String communityName;

    public String getCommunityId()
    {
        return communityId;
    }

    public void setCommunityId(String communityId)
    {
        this.communityId = communityId;
    }

    public String getCommunityName()
    {
        return communityName;
    }

    public void setCommunityName(String communityName)
    {
        this.communityName = communityName;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Community other = (Community) obj;
        if (this.communityId != other.communityId && (this.communityId == null || !this.communityId.equals(other.communityId)))
        {
            return false;
        }
        if (this.communityName != other.communityName && (this.communityName == null || !this.communityName.equals(other.communityName)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 73 * hash + (this.communityId != null ? this.communityId.hashCode() : 0);
        hash = 73 * hash + (this.communityName != null ? this.communityName.hashCode() : 0);
        return hash;
    }
}
