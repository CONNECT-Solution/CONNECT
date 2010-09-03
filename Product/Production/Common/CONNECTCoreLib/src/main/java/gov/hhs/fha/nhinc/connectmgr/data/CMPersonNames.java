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
 * @created 20-Oct-2008 12:07:20 PM
 */
public class CMPersonNames
{

    private List<String> personNameList = new ArrayList<String>();

    /**
     * Default constructor.
     */
    public CMPersonNames()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        personNameList = new ArrayList<String>();
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMPersonNames oCompare)
    {
        if (oCompare.personNameList.size() != this.personNameList.size())
        {
            return false;
        }
        
        int iCnt = this.personNameList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.personNameList.get(i).equals(oCompare.personNameList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    

    /**
     * Returns the names for this person.
     * 
     * @return The list of names for this person.
     */
    public List<String> getPersonName()
    {
        return personNameList;
    }
}