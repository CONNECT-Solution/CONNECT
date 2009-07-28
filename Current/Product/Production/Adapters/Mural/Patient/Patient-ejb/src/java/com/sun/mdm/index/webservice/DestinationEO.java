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

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.exception.ObjectException;

public class DestinationEO{
    public static final int version = 1;

    private EnterpriseObject mEnterprise;

    /**
     * Creates a new DestinationEO instance.
     */
    public DestinationEO() {
    }
    
    /**
     * Creates a new DestinationEO instance from a EnterpriseObject object.
     */
    protected DestinationEO(EnterpriseObject eo) {
            mEnterprise = eo;
    }
    
    /**
     * Getter for enterprisePatient attribute.
     */
    public EnterprisePatient getEnterprisePatient() 
        throws ObjectException {
        return new EnterprisePatient(mEnterprise);
    }
 
    /**
     * Setter for enterprisePatient attribute.
     * @param eo EnterprisePatient
     */
    public void setEnterprisePatient(EnterprisePatient eo) 
        throws ObjectException {
        mEnterprise = eo.pGetEnterpriseObject();
    } 

}