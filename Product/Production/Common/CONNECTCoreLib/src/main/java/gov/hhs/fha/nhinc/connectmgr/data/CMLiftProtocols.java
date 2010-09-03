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
public class CMLiftProtocols {
    private List<String> protocolList = new ArrayList<String>();

     /**
     * Default Constructor.
     */
    public CMLiftProtocols()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default protocol.
     */
    public void clear()
    {
        protocolList = new ArrayList<String>();
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     *
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMLiftProtocols oCompare)
    {
        if (oCompare.protocolList == null && this.protocolList == null) {
            return true;
        }
        else if (oCompare.protocolList == null || this.protocolList == null) {
            return false;
        }
        else if (oCompare.protocolList.size() != this.protocolList.size()) {
            return false;
        }

        int iCnt = this.protocolList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.protocolList.get(i).equalsIgnoreCase(oCompare.protocolList.get(i)))
            {
                return false;
            }
        }

        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }



    /**
     * Returns the list of protocols.
     *
     * @return The list of protocols.
     */
    public List<String> getProtocol()
    {
        return protocolList;
    }

    /**
     * Sets the list of protocols.
     *
     * @return void
     */
    public void setProtocol(List<String> protocolList)
    {
        this.protocolList = protocolList;
    }

    /**
     * Creates a deep copy of this object.
     *
     * @return A copy of this CMLiftProtocols object
     */
    public CMLiftProtocols createCopy() {
        CMLiftProtocols liftProtocolsCopy = new CMLiftProtocols();
        liftProtocolsCopy.getProtocol().addAll(protocolList);
        return liftProtocolsCopy;
    }
}
