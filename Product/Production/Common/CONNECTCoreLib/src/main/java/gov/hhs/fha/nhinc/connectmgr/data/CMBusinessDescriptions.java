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
 * This class contains one or more descriptions for the business entity.
 * 
 * @author Les Westberg
 */
public class CMBusinessDescriptions
{
    private List<String> businessDescriptionList = new ArrayList<String>();

    /**
     * Default Constructor.
     */
    public CMBusinessDescriptions()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        businessDescriptionList = new ArrayList<String>();
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBusinessDescriptions oCompare)
    {
        if (oCompare.businessDescriptionList.size() != this.businessDescriptionList.size())
        {
            return false;
        }
        
        int iCnt = this.businessDescriptionList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.businessDescriptionList.get(i).equals(oCompare.businessDescriptionList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }

    /**
     * Returns the list of business descriptions for this entity.
     * 
     * @return The list of business descriptions for this entity.
     */
    public List<String> getBusinessDescription()
    {
        return businessDescriptionList;
    }
    
    
}