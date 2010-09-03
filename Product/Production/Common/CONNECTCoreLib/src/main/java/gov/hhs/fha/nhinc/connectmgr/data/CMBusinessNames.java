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
 * @author westbergl
 * @version 1.0
 * @created 20-Oct-2008 12:06:51 PM
 */
public class CMBusinessNames
{

    private List<String> businessNameList = new ArrayList<String>();

    /**
     * Default constructor.
     */
    public CMBusinessNames()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        businessNameList = new ArrayList<String>();
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBusinessNames oCompare)
    {
        if (oCompare.businessNameList.size() != this.businessNameList.size())
        {
            return false;
        }
        
        int iCnt = this.businessNameList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.businessNameList.get(i).equals(oCompare.businessNameList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    

    /**
     * Return the list of business names for this business.
     * 
     * @return The list of business names for this business.
     */
    public List<String> getBusinessName()
    {
        return businessNameList;
    }
    
}