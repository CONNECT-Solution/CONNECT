/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a set of business services for a business entity.
 * 
 * @author Les Westberg
 */
public class CMBusinessServices
{
    private List<CMBusinessService> businessServiceList = new ArrayList<CMBusinessService>();

    /**
     * Default Constructor.
     */
    public CMBusinessServices()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        businessServiceList = new ArrayList<CMBusinessService>();
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBusinessServices oCompare)
    {
        if (oCompare.businessServiceList.size() != this.businessServiceList.size())
        {
            return false;
        }
        
        int iCnt = this.businessServiceList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.businessServiceList.get(i).equals(oCompare.businessServiceList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    /**
     * Returns a list of business services for this business entity.
     * 
     * @return The list of business services for this business entity.
     */
    public List<CMBusinessService> getBusinessService()
    {
        return businessServiceList;
    }
    
}