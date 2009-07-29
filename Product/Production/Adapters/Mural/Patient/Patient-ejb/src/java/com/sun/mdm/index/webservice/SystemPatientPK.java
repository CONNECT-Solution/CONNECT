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
import com.sun.mdm.index.webservice.SystemObjectPKBean;

public class SystemPatientPK implements SystemObjectPKBean{

    public static final int version = 1;
    private SystemObjectPK mSystemObjectPK;
    
    /**
     * No argument constructor.
     */
    public SystemPatientPK() {
    }
    
    /**
     * Creates a new SystemPatientPK instance from a SystemObjectPK.
     */
    public SystemPatientPK(SystemObjectPK sysObjPK) {
       mSystemObjectPK = sysObjPK;
    }
           
    /**
     * Getter for localID
     * @return String lid
     */
    public String getLocalId() {
       return mSystemObjectPK.lID; 
    }

    /**
     * Getter for systemCode
     * @return String system code
     */
    public String getSystemCode() {
       return mSystemObjectPK.systemCode;
    }
       
    /**
     * Setter for localID
     * @param lid Local ID
     */
    public void setLocalId(String lid) {
       mSystemObjectPK.lID = lid; 
    }

    /**
     * Setter for systemCode
     * @param code system code
     */
    public void setSystemCode(String code) {
       mSystemObjectPK.systemCode = code;
    }
}
