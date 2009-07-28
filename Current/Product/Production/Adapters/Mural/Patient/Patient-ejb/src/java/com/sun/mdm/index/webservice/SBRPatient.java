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

public class SBRPatient implements SBRObjectBean{
    public static final int version = 1;
     
    private SBR msbr;
    private PatientBean mPatient;

    /**
     * No argument constructor.
     */ 
    public SBRPatient() {
    }
    
    /**
     * Creates a new SBRPatient instance from a SBR.
     */
    public SBRPatient(SBR sbr) throws ObjectException {
      msbr = sbr; 
      mPatient = new PatientBean((PatientObject)sbr.getObject());
    }

    /**
     * Getter for SBR object
     * @return SBR object
     */
    public SBR pGetSBRObject() {
       return msbr;
    }
        
    /**
     * Getter for PatientBean
     * @return Patient object
     * @exception ObjectException Object Exception
     */
    public PatientBean getPatient() {
       return mPatient;  
    }

    /**
     * Return ObjectBean
     * @return object bean
     * @exception ObjectException Object Exception
     */
    public ObjectBean pGetObjectBean() {
       return getPatient();  
    }
    
    /**
     * Getter for status
     * @exception ObjectException object exception
     * @return status status
     */
    public String getStatus()  throws ObjectException {
      return msbr.getStatus();
    }

    /**
     * Setter for Patient
     * @param obj object
     * @exception ObjectException object exception
     */
    public void setPatient(PatientBean aPatient)
        throws ObjectException {
         msbr.setObject(aPatient.pGetObject()); 
         mPatient = aPatient;        
    }

    /**
     * Setter for status
     * @param status status
     * @exception ObjectException object exception
     */
    public void setStatus(String status) throws ObjectException {
      msbr.setStatus(status);
    }
    
}