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
public class CMInternalConnectionInfoLiftProtocols {
    private List<CMInternalConnectionInfoLiftProtocol> protocolList = new ArrayList<CMInternalConnectionInfoLiftProtocol>();

    /**
     * Default constructor.
     */
    public CMInternalConnectionInfoLiftProtocols()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default protocol.
     */
    public void clear()
    {
        protocolList = new ArrayList<CMInternalConnectionInfoLiftProtocol>();
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     *
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMInternalConnectionInfoLiftProtocols oCompare)
    {
        if (oCompare.protocolList.size() != this.protocolList.size())
        {
            return false;
        }

        int iCnt = this.protocolList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.protocolList.get(i).equals(oCompare.protocolList.get(i)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Return the list of protocols associated with this home community.
     *
     * @return The list of protocols associated with this home community.
     */
    public List<CMInternalConnectionInfoLiftProtocol> getProtocol()
    {
        return protocolList;
    }
}
