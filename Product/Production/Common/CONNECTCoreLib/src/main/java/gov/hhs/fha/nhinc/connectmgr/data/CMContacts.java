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
 * This class represents a list of contacts for a business entity.
 * 
 * @author Les Westberg
 */
public class CMContacts
{
    private List<CMContact> contactList = new ArrayList<CMContact>();

    /**
     * Default constructor.
     */
    public CMContacts()
    {
        clear();
    }
    
    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        contactList = new ArrayList<CMContact>();
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMContacts oCompare)
    {
        if (oCompare.contactList.size() != this.contactList.size())
        {
            return false;
        }
        
        int iCnt = this.contactList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.contactList.get(i).equals(oCompare.contactList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }

    /**
     * Returns the list of contacts.
     * 
     * @return The list of contacts.
     */
    public List<CMContact> getContact()
    {
        return contactList;
    }

}