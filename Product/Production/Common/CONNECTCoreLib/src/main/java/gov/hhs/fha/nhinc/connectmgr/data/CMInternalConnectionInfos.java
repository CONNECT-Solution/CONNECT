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
import java.util.List;
import java.util.ArrayList;

/**
 * @author Les Westberg
 */
public class CMInternalConnectionInfos {
    private List<CMInternalConnectionInfo> internalConnectionInfoList = new ArrayList<CMInternalConnectionInfo>();

    /**
     * Default Constructor.
     */
    public CMInternalConnectionInfos() {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear() {
        internalConnectionInfoList = new ArrayList<CMInternalConnectionInfo>();
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        if (internalConnectionInfoList != null) {
            hashCode = internalConnectionInfoList.hashCode();
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
        
        if (!(object instanceof CMInternalConnectionInfos))
            return false;
        CMInternalConnectionInfos oCompare = (CMInternalConnectionInfos) object;

        if (oCompare.internalConnectionInfoList.size() != this.internalConnectionInfoList.size()) {
            return false;
        }

        int iCnt = this.internalConnectionInfoList.size();
        for (int i = 0; i < iCnt; i++) {
            if (!this.internalConnectionInfoList.get(i).equals(oCompare.internalConnectionInfoList.get(i))) {
                return false;
            }
        }

        // If we got here then everything is the same...
        // ----------------------------------------------
        return true;
    }

    /**
     * This returns a list of InternalConnectionInfo objects.
     * 
     * @return The list of internal conneciton info objects.
     */
    public List<CMInternalConnectionInfo> getInternalConnectionInfo() {
        return internalConnectionInfoList;
    }

}