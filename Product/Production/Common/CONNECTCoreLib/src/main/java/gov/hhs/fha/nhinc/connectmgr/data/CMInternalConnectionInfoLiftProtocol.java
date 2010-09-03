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

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author JHOPPESC
 */
public class CMInternalConnectionInfoLiftProtocol {
    String name;

     /**
     * Default constructor.
     */
    public CMInternalConnectionInfoLiftProtocol() {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default protocol.
     */
    public void clear() {
        name = "";
    }

     /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     *
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMInternalConnectionInfoLiftProtocol oCompare) {
        boolean result = false;

        if (NullChecker.isNullish(oCompare.name) &&
                NullChecker.isNullish(this.name)) {
            result = true;
        }
        else if (NullChecker.isNullish(oCompare.name) ||
                NullChecker.isNullish(this.name)) {
            result = false;
        }
        else {
            if (this.name.equalsIgnoreCase(oCompare.name)) {
                result = true;
            }
        }

        return result;
    }

    /**
     * Return the protocol name.
     *
     * @return The protocol name.
     */
    public String getLiftProtocol() {
        return name;
    }

    /**
     * Set the protocol value.
     *
     * @param protocol The protocol value.
     */
    public void setLiftProtocol(String name) {
        this.name = name;
    }

}
