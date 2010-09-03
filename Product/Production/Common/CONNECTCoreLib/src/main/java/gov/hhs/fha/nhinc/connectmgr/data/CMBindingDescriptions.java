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
 * This class is used to contain the list of descriptions for a binding.
 * 
 * @author Les Westberg
 */
public class CMBindingDescriptions
{
    private List<String> descriptionList = new ArrayList<String>();

    /**
     * Default constructor.
     */
    public CMBindingDescriptions()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        descriptionList = new ArrayList<String>();
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBindingDescriptions oCompare)
    {
        if (oCompare.descriptionList.size() != this.descriptionList.size())
        {
            return false;
        }
        
        int iCnt = this.descriptionList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.descriptionList.get(i).equals(oCompare.descriptionList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    

    /**
     * Returns the list of descriptions for this binding.
     * 
     * @return The descriptions for this binding.
     */
    public List<String> getDescription()
    {
        return descriptionList;
    }
    
    /**
     * Creates a deep copy of this object.
     *
     * @return A copy of this CMBindingDescriptions object
     */
    public CMBindingDescriptions createCopy() {
        CMBindingDescriptions descriptionCopy = new CMBindingDescriptions();
        descriptionCopy.getDescription().addAll(descriptionList);
        return descriptionCopy;
    }
}