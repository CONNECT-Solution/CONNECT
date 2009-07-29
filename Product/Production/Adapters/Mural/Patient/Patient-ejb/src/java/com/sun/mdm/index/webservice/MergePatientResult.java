/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */

package com.sun.mdm.index.webservice;

import com.sun.mdm.index.objects.*;
import com.sun.mdm.index.objects.exception.*;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.webservice.EnterpriseObjectMessage;

/**
 * Return value from Patient merge related functions.
 */

public class MergePatientResult {
    public static final int version = 1;
    
    private DestinationEO mDestEO;
    private SourceEO mSourceEO;
    
    /**
     * No argument constructor.
     */ 
    public MergePatientResult() {
    }
    
    /**
     * Creates a new MergePatientResult instance from a MergeResult
     */ 
    protected MergePatientResult(MergeResult mergeResult) {
       mDestEO = new DestinationEO(mergeResult.getDestinationEO());
       mSourceEO = new SourceEO(mergeResult.getSourceEO());
    }
            
    /** 
     * Get destination EO
     * @return Destination EO
     */
    public DestinationEO getDestinationEO() {
        return mDestEO;
    }

    /** 
     * Get Source EO
     * @return Source EO
     */
    public SourceEO getSourceEO() {
        return mSourceEO;
    }

    /** 
     * Set destination EO
     * @param eom DestinationEO 
     */
    public void setDestinationEO(DestinationEO eom) {
        mDestEO = eom;
    }
    
    /** 
     * Set Source EO
     * @param eom SourceEO 
     */
    public void setSourceEO(SourceEO eom) {
        mSourceEO = eom;
    }
}
