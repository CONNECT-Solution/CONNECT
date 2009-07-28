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

import com.sun.mdm.index.webservice.SearchObjectResult;
import com.sun.mdm.index.webservice.ObjectBean;

/** 
 * Contains the Search Result invoked from Patient.search method.
 */
public final class SearchPatientResult implements SearchObjectResult{
    public static final int version = 1;
    
    private String meuid;
    private float mcomparison;
    private PatientBean mPatient;
    
    /**
     * No argument constructor.
     */
    public SearchPatientResult() {
    }
    
    /**
     * Creates a new SearchPatientResult instance from a PatientBean
     * and euid and comparisonScore.
     */ 
    public SearchPatientResult(PatientBean bean, String euid, float comparisonScore ) {
       meuid = euid;
       mcomparison = comparisonScore;
       mPatient = bean;
    }
    
    /**
     * Getter for EUID
     * @return euid
     */
    public String getEUID() {
       return meuid;
    }
    
    /**
     * Getter for Comparison Score
     * @return Comparison Score
     */
    public float getComparisonScore() {
       return mcomparison;
    }

    /**
     * Getter for Patient
     * @return PatientBean
     */
    public PatientBean getPatient() {
      return mPatient;
    }

    /**
     * Return for object bean
     * @return object bean 
     */
    public ObjectBean pGetObjectBean() {
       return getPatient();
    }

    /**
     * Setter for EUID
     * @param euid String
     */
    public void setEUID(String euid) {
       meuid = euid;
    }
    
    /**
     * Setter for ComparisonScore
     * @param score float
     */
    public void setComparisonScore(float score) {
       mcomparison = score;
    }
    
    /**
     * Setter for Patient
     * @param bean PatientBean
     */
    public void setPatient(PatientBean bean) {
       mPatient = bean;
    }
}
