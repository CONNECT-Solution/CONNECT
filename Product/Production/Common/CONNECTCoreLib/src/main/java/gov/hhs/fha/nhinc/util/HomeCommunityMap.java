/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.util;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;

/**
 * This class is used to map a home community ID to the
 * textual name of the home community.  The information
 * is stored in a properties file so that it can be tweaked
 * and changed without having to recompile...
 * 
 * @author Les Westberg
 */
public class HomeCommunityMap 
{
    private static Log log = LogFactory.getLog(HomeCommunityMap.class);
    
    /**
     * This method retrieves the name of the home community baased on the
     * home community Id.
     * 
     * @param sHomeCommunityId The home community ID to be looked up.
     * @return The textual name of the home community.
     */
    public String getHomeCommunityName(String sHomeCommunityId)
    {
        String sHomeCommunityName = "";
        
        try
        {
            CMBusinessEntity oEntity = ConnectionManagerCache.getBusinessEntity(sHomeCommunityId);
            if ((oEntity != null) &&
                (oEntity.getNames() != null) &&
                (oEntity.getNames().getBusinessName() != null) &&
                (oEntity.getNames().getBusinessName().size() > 0) &&
                (oEntity.getNames().getBusinessName().get(0) != null) &&
                (oEntity.getNames().getBusinessName().get(0).length() > 0))
            {
                sHomeCommunityName = oEntity.getNames().getBusinessName().get(0);
            }
        }
        catch (Exception e)
        {
            log.warn("Failed to retrieve textual name for home community ID: " + sHomeCommunityId, e);
        }
        
        return sHomeCommunityName;
    }
}
