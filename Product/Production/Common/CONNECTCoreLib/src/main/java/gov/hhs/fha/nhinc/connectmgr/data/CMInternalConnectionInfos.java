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
 * @author Les Westberg
 */
public class CMInternalConnectionInfos
{
    private List<CMInternalConnectionInfo> internalConnectionInfoList = new ArrayList<CMInternalConnectionInfo>();

    /**
     * Default Constructor.
     */
    public CMInternalConnectionInfos()
    {
        clear();
    }
    
    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        internalConnectionInfoList = new ArrayList<CMInternalConnectionInfo>();
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMInternalConnectionInfos oCompare)
    {
        if (oCompare.internalConnectionInfoList.size() != this.internalConnectionInfoList.size())
        {
            return false;
        }
        
        int iCnt = this.internalConnectionInfoList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.internalConnectionInfoList.get(i).equals(oCompare.internalConnectionInfoList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    

    /**
     * This returns a list of InternalConnectionInfo objects.
     * 
     * @return The list of internal conneciton info objects.
     */
    public List<CMInternalConnectionInfo> getInternalConnectionInfo()
    {
        return internalConnectionInfoList;
    }

}