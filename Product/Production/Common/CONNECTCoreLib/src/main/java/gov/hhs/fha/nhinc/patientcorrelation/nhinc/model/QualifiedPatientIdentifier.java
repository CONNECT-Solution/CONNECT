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

package gov.hhs.fha.nhinc.patientcorrelation.nhinc.model;

/**
 *
 * @author rayj
 */
public class QualifiedPatientIdentifier {
    private String assigningAuthorityId;
    private String patientId;

    public String getAssigningAuthorityId() {
        return assigningAuthorityId;
    }

    public void setAssigningAuthority(String assigningAuthorityId) {
        this.assigningAuthorityId = assigningAuthorityId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }  
}
