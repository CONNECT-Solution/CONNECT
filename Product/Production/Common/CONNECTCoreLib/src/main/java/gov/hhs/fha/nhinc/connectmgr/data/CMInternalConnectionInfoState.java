/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.connectmgr.data;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 * 
 * @author JHOPPESC
 */
public class CMInternalConnectionInfoState {

    private String name;

    /**
     * Default constructor.
     */
    public CMInternalConnectionInfoState() {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear() {
        name = "";
    }
    @Override
    public int hashCode() {
        int hashCode = 0;
        if (name != null) {
            hashCode = name.hashCode();
        }
        return hashCode;
    }

    /**
     * Returns true of the contents of the object are the same as the one passed in.
     * 
     * @param object The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(Object object) {
        boolean result = false;
        if (!(object instanceof CMInternalConnectionInfoState))
            return false;
        
        CMInternalConnectionInfoState oCompare = (CMInternalConnectionInfoState) object;
        if (NullChecker.isNullish(oCompare.name) && NullChecker.isNullish(this.name)) {
            result = true;
        } else if (NullChecker.isNullish(oCompare.name) || NullChecker.isNullish(this.name)) {
            result = false;
        } else {
            if (this.name.equalsIgnoreCase(oCompare.name)) {
                result = true;
            }
        }

        return result;
    }

    /**
     * Return the name of this servie.
     * 
     * @return The name of this service.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this service.
     * 
     * @param name The name of this service.
     */
    public void setName(String name) {
        this.name = name;
    }
}
