/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JHOPPESC
 */
public class CMInternalConnectionInfoStates {
    private List<CMInternalConnectionInfoState> stateList = new ArrayList<CMInternalConnectionInfoState>();

    /**
     * Default constructor.
     */
    public CMInternalConnectionInfoStates()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        stateList = new ArrayList<CMInternalConnectionInfoState>();
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     *
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMInternalConnectionInfoStates oCompare)
    {
        if (oCompare.stateList.size() != this.stateList.size())
        {
            return false;
        }

        int iCnt = this.stateList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.stateList.get(i).equals(oCompare.stateList.get(i)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Return the list of states associated with this home community.
     *
     * @return The list of states associated with this home community.
     */
    public List<CMInternalConnectionInfoState> getState()
    {
        return stateList;
    }

}
